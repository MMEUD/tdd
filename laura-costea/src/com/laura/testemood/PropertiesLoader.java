package com.laura.testemood;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import com.laura.testemood.sorter.SortByValueAndKeyStrategy;
import com.laura.testemood.sorter.SortMapBySpecificValue;
import com.laura.testemood.sorter.SorterContext;

public final class PropertiesLoader {

	private static final PropertiesLoader instance = new PropertiesLoader();

	private Map<String, String> properties = new ConcurrentHashMap<String, String>();

	private Map<String, String> propertySource = new ConcurrentHashMap<String, String>();

	private PropertiesLoader() {
	}

	public static PropertiesLoader getInstance() {
		return instance;
	}

	public String load(String namespace) {
		Properties tmp = new Properties();
		try {

			tmp.load(new FileInputStream(new File(namespace + ".properties")));
			Iterator<Object> it = tmp.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				setProperty(key.split("\\.")[0], tmp.getProperty(key),
						namespace);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(tmp.size());
	}

	public String list(String namespace) {
		StringBuffer sb = new StringBuffer();
		SorterContext ctx = new SorterContext();
		ctx.setSortingStrategy(new SortByValueAndKeyStrategy());
		Map<String, String> tmp = ctx.sortMap(propertySource);
		Iterator<String> it = tmp.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (namespace == null
					|| (namespace != null && tmp.get(key).equalsIgnoreCase(
							namespace))) {
				sb.append(tmp.get(key) + ":" + key.split("\\.")[0] + "="
						+ properties.get(key) + "\n");
			}
		}
		return sb.toString();
	}

	public HashMap<String, String> save(String namespace) {
		Properties p = new Properties();
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			if (namespace != null) {
				result.put(namespace,String.valueOf(storeNamespace(namespace)));

			} else {
				Set<String> namespaceSet = new HashSet<String>();
				namespaceSet.addAll(propertySource.values());
				Iterator<String> it = namespaceSet.iterator();
				while (it.hasNext()) {
					namespace = it.next();
					result.put(namespace,String.valueOf(storeNamespace(namespace)));
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private int storeNamespace(String namespace) throws FileNotFoundException,
			IOException {
		Properties p = new Properties();
		Set<String> keyset = new TreeSet<String>(SortMapBySpecificValue.getKeysByValue(
				propertySource, namespace));
		for (String entry : keyset) {
			p.put(entry, properties.get(entry));
		}
		p.store(new FileOutputStream(new File(namespace + ".properties")), null);
		return p.size();
	}

	public void setPropertySource(String propertyName, String fileName) {
		propertySource.put(propertyName, fileName);
	}

	public void setProperty(String propertyName, String propertyValue,
			String namespace) {
		setPropertySource(propertyName + "." + namespace, namespace);
		set(propertyName + "." + namespace, propertyValue);
	}

	public String getProperty(String propertyName, String namespace) {
		return get(propertyName + "." + namespace);
	}

	private String get(String name) {
		if (properties.get(name) == null) {
			System.out.println("Parameter " + name + " is missing.");
		}
		return properties.get(name);
	}

	private void set(String name, String value) {
		properties.put(name, value);
	}

}
