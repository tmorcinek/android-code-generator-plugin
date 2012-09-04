package com.morcinek.android.codegenerator.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.morcinek.android.codegenerator.logic.util.ActivityResource;
import com.morcinek.android.codegenerator.logic.util.WidgetResource;
import com.morcinek.android.codegenerator.serialization.Method;
import com.morcinek.android.codegenerator.serialization.Method.Parameter;
import com.morcinek.android.codegenerator.serialization.Type;
import com.morcinek.android.codegenerator.serialization.Type.Require;
import com.morcinek.android.codegenerator.serialization.Type.Require.Listener;

public class ActivityObject implements ActivityGeneratorInterface {

	private boolean shortMode = false;

	protected TypesAdapter typesAdapter;

	private String packageName;

	private ActivityResource activityResource;

	private Set<String> imports = new HashSet<String>();

	private Set<String> interfaces = new HashSet<String>();

	private Map<WidgetResource, Type> widgetsTypes = new HashMap<WidgetResource, Type>();

	public ActivityObject(Boolean pShortMode) {
		shortMode = pShortMode;
		imports.add("Activity");
		imports.add("Bundle");
	}

	public void setTypesAdapter(TypesAdapter typesAdapter) {
		this.typesAdapter = typesAdapter;
	}

	public String getImports() {
		StringBuilder stringBuilder = new StringBuilder();
		for (String widgetName : imports) {
			String importLine = "import " + typesAdapter.getTypeFullName(widgetName) + ";\n";
			stringBuilder.append(importLine);
		}
		return stringBuilder.toString();
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackage() {
		return "package " + packageName + ";\n";
	}

	public void setActivityResource(ActivityResource pActivityResource) {
		activityResource = pActivityResource;
	}

	public void addWidget(WidgetResource widgetResource, String typeName) {
		Type type = typesAdapter.getType(typeName);
		if(type == null){
			throw new IllegalArgumentException("Type: " + typeName + " is not defined.");
		}
		widgetsTypes.put(widgetResource, type);
		imports.add(typeName);
		if (type.getRequire() != null) {
			for (Listener listener : type.getRequire().getListener()) {
				interfaces.add(listener.getType());
				imports.add(listener.getType());
				// adding method parameters to import
//				Type listenerType = typesAdapter.getType(listener.getType());
//				if(listenerType.getImplements() != null){
//					for (Method iterable_element : iterable) {
//						
//					}
//				}
			}
		}
	}

	
	public String getHeader() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("public class %s extends Activity ", activityResource.getVariableName()));
		String[] interfacesArray = interfaces.toArray(new String[0]);
		for (int i = 0; i < interfacesArray.length; i++) {
			if (i == 0) {
				stringBuilder.append("implements ");
			} else {
				stringBuilder.append(", ");
			}
			stringBuilder.append(interfacesArray[i]);
		}
		stringBuilder.append("{\n");
		return stringBuilder.toString();
	}

	
	public String getFields() {
		StringBuilder stringBuilder = new StringBuilder();
		for (WidgetResource widgetResource : widgetsTypes.keySet()) {
			String fieldLine = "\tprivate " + widgetsTypes.get(widgetResource).getName() + " "
					+ widgetResource.getVariableName() + ";\n";
			stringBuilder.append(fieldLine);
		}
		return stringBuilder.toString();
	}

	
	public String getConstants() {
		// TODO Auto-generated method stub
		return "";
	}

	
	public String getCreateMethod() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\t@Override\n");
		stringBuilder.append("\tpublic void onCreate(Bundle savedInstanceState) {\n");
		stringBuilder.append("\t\tsuper.onCreate(savedInstanceState);\n");
		stringBuilder.append(String.format("\t\tsetContentView(%s);\n", activityResource.getReference()));
		stringBuilder.append("\n");
		stringBuilder.append(getDeclaration());
		stringBuilder.append("\t}\n");
		return stringBuilder.toString();
	}
	
	private String getMethodBody(Method method,String interfaceName){
		StringBuilder stringBuilder = new StringBuilder();	
		Set<Type> connectedTypes = typesAdapter.getTypesFromListener(interfaceName);
		int i = 0;
		for (WidgetResource widgetResource : widgetsTypes.keySet()) {
			if(connectedTypes.contains(widgetsTypes.get(widgetResource))){
				if (i == 0) {
					stringBuilder.append("\t\t");
				} else {
					stringBuilder.append(" else ");					
				}
				stringBuilder.append(String.format("if(%s.getId() == %s){\n", getFirstLowerCase(method.getParameter().get(0).getType()),widgetResource.getReference()));
				stringBuilder.append("\n");
				stringBuilder.append("\t\t}");
				i++;
			}
		}
		stringBuilder.append("\n");
		
		return stringBuilder.toString();
	}

	private String getMethod(Method method, String interfaceName) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\t@Override\n");
		stringBuilder.append(String.format("\tpublic %s %s(", (method.getType() != null) ? method.getType() : "void",
				method.getName()));
		int i = 0;
		for (Parameter parameter : method.getParameter()) {
			if (i != 0) {
				stringBuilder.append(", ");
			}
			String typeName = parameter.getType();
			stringBuilder.append(typeName + " ");
			if(typesAdapter.getType(typeName) != null){
				stringBuilder.append(getFirstLowerCase(typeName));				
			}else {
				stringBuilder.append("arg" + i);
			}
			i++;
		}
		stringBuilder.append(") {\n");
		stringBuilder.append("\n");
		stringBuilder.append(getMethodBody(method, interfaceName));
		stringBuilder.append("\t}\n");
		return stringBuilder.toString();
	}

	private static String getFirstLowerCase(String word) {
		return Character.toLowerCase(word.charAt(0)) + word.substring(1);
	}

	
	public String getMethods() {
		Map<Method,String> interfacesMethods = new HashMap<Method,String>();
		for (String typeName : interfaces) {
			List<Method> typeMethods = typesAdapter.getTypeMethods(typeName);
			for (Method method : typeMethods) {
				interfacesMethods.put(method, typeName);
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (Entry<Method, String> entry : interfacesMethods.entrySet()) {
			stringBuilder.append(getMethod(entry.getKey(),entry.getValue()));
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	
	public String getDeclaration() {
		StringBuilder stringBuilder = new StringBuilder();
		for (WidgetResource widgetResource : widgetsTypes.keySet()) {
			String declarationLine;
			Require require = widgetsTypes.get(widgetResource).getRequire();
			if (shortMode) {
				// ((Button)findViewById(R.id.my_button)).setOnClickListener(this);
				if (require != null) {
					for (Listener listener : require.getListener()) {
						declarationLine = String.format("\t\t((%s) findViewById(%s)).set%s(this);\n",
								widgetsTypes.get(widgetResource).getName(), widgetResource.getReference(),
								listener.getType());
						stringBuilder.append(declarationLine);
					}
				}
			} else {
				// Button myButton = (Button) findViewById(R.id.my_button);
				declarationLine = String.format("\t\t%2$s = (%1$s) findViewById(%3$s);\n",
						widgetsTypes.get(widgetResource).getName(), widgetResource.getVariableName(),
						widgetResource.getReference());
				stringBuilder.append(declarationLine);
				if (require != null) {
					for (Listener listener : require.getListener()) {
						// myButton.setOnClickListener(this);
						declarationLine = String.format("\t\t%s.set%s(this);\n", widgetResource.getVariableName(),
								listener.getType());
						stringBuilder.append(declarationLine);
					}
				}
			}
		}
		return stringBuilder.toString();
	}

	
	public String generate() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getPackage());
		stringBuilder.append("\n");
		stringBuilder.append(getImports());
		stringBuilder.append("\n");
		stringBuilder.append(getHeader());
		stringBuilder.append("\n");
		if (!shortMode) {
			stringBuilder.append(getFields());
			stringBuilder.append("\n");
		}
		stringBuilder.append(getCreateMethod());
		stringBuilder.append("\n");
		stringBuilder.append(getMethods());
		stringBuilder.append("}");
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}

}
