package io.github.deathcap.bukkit2sponge.plugin;

import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;

public class ShinyClassLoader extends URLClassLoader {

    private static DefaultShader shader = null;

    static {
        List<Relocator> relocators = new ArrayList<>();
        Relocator relocator = new Relocator("com/google/common", "io/github/deathcap/bukkit2sponge/libs/guava17/com/google/common", null, null, true);
        relocators.add(relocator);

        try {
            shader = new DefaultShader(relocators);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ShinyClassLoader(URL[] urls, ClassLoader parent) throws IOException {
        super(urls, parent);
    }

    private byte[] relocateBytecode(byte[] bytes) throws IOException {
        return shader.getRemappedClass(bytes);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
        URL url = this.findResource(path);
        if (url == null) {
            throw new ClassNotFoundException("Unable to find class resource " + name + " at " + path);
        }

        InputStream stream = null;
        byte[] bytecode = null;
        try {
            stream = url.openStream();
            bytecode = ByteStreams.toByteArray(stream);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ClassNotFoundException("Unable to open or read stream for class " + name + ": " + ex.getLocalizedMessage());
        }

        CodeSource codeSource = null;
        try {
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            URL jarURL = jarURLConnection.getJarFileURL();
            codeSource = new CodeSource(jarURL, new CodeSigner[0]);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ClassNotFoundException("Unable to read code source for class " + name + ": " + ex.getLocalizedMessage());
        }

        byte[] remappedBytecode;
        try {
            remappedBytecode = relocateBytecode(bytecode);
        } catch (IOException ex) {
            ex.printStackTrace();
            //throw new ClassNotFoundException("Unable to relocate class " + name + ": " + ex.getLocalizedMessage());
            remappedBytecode = bytecode;
        }

        Class<?> result = this.defineClass(name, remappedBytecode, 0, remappedBytecode.length, codeSource);
        if (result != null) {
            this.resolveClass(result);
        }

        return result;
    }
}
