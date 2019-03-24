package com.gary.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class PackageScanner {
	
	public PackageScanner() {
	}
	
	public abstract void dealClass(Class<?> klass);
	
	private void scanPackage(String packageName, File currentFile) {
		File[] fileList = currentFile.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return true;
				}
				return pathname.getName().endsWith(".class");
			}
		});
		
		for (File file : fileList) {
			if (file.isDirectory()) {
				scanPackage(packageName + "." + file.getName(), file);
			} else {
				String fileName = file.getName().replace(".class", "");
				String className = packageName + "." + fileName;
				try {
					Class<?> klass = Class.forName(className);
					if (klass.isAnnotation() 
							|| klass.isInterface()
							|| klass.isEnum()
							|| klass.isPrimitive()) {
						continue;
					}
					dealClass(klass);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (ExceptionInInitializerError e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void scanPackage(URL url) throws IOException {
		JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
		JarFile jarFile = urlConnection.getJarFile();
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			String jarName = jarEntry.getName();
			if (jarEntry.isDirectory() || !jarName.endsWith(".class")) {
				continue;
			}
			String className = jarName.replace(".class", "").replaceAll("/", ".");
			try {
				Class<?> klass = Class.forName(className);
				if (klass.isAnnotation() 
						|| klass.isInterface()
						|| klass.isEnum()
						|| klass.isPrimitive()) {
					continue;
				}
				dealClass(klass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void packageScan(Class<?>[] klasses) {
		for (Class<?> klass : klasses) {
			packageScan(klass);
		}
	}
	
	public void packageScan(Class<?> klass) {
		packageScan(klass.getPackage().getName());
	}
	
	public void packageScan(String[] packages) {
		for (String packageName : packages) {
			packageScan(packageName);
		}
	}
	
	public void packageScan(String packageName) {
		String packageOpperPath = packageName.replace(".", "/");
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			Enumeration<URL> resources = classLoader.getResources(packageOpperPath);
			while (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				if (url.getProtocol().equals("jar")) {
					scanPackage(url);
				} else {
					File file = new File(url.toURI());
					if (!file.exists()) {
						continue;
					}
					scanPackage(packageName, file);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
