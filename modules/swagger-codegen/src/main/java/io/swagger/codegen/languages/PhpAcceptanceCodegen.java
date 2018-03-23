package io.swagger.codegen.languages;

import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.CodegenParameter;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PhpAcceptanceCodegen extends AbstractPhpCodegen
{
     @SuppressWarnings("hiding")
    protected String apiVersion = "1.0.0";
        
    /**
     * Configures the type of generator.
     * 
     * @return  the CodegenType for this generator
     * @see     io.swagger.codegen.CodegenType
     */
    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    /**
     * Configures a friendly name for the generator.  This will be used by the generator
     * to select the library with the -l flag.
     * 
     * @return the friendly name for the generator
     */
    public String getName() {
        return "php-acceptance";
    }

    /**
     * Returns human-friendly help for the generator.  Provide the consumer with help
     * tips, parameters here
     * 
     * @return A string value for the help message
     */
    public String getHelp() {
        return "Generates a LumenServerCodegen server library.";
    }

    public PhpAcceptanceCodegen() {
        super();
        acceptanceTemplateFiles.put("acceptance.handlebars", ".php");
        embeddedTemplateDir = templateDir = "php_acceptance";

        /*
         * packPath
         */
        packagePath = "";
        srcBasePath = "";

        // template files want to be ignored
        apiTemplateFiles.clear();
        modelTemplateFiles.clear();
        apiTestTemplateFiles.clear();
        apiDocTemplateFiles.clear();
        modelDocTemplateFiles.clear();
    }

    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
        objs = super.postProcessOperations(objs);
        Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
        List<CodegenOperation> ops = (List<CodegenOperation>) operations.get("operation");

        int operationCounter = 0;
        
        for (CodegenOperation operation : ops) {
            String path = operation.path;
            if (operation.pathParams.size() > 0) {
                for (CodegenParameter pathParam : operation.pathParams) {
                    path = path.replace("{" + pathParam.baseName + "}", "$" + pathParam.baseName);
                }
            }

            CodegenOperation pathResolver = operation;
            pathResolver.resolvePath = path;
            ops.set(operationCounter, pathResolver);

            if (operation.httpMethod.equals("POST")) {
                operations.put("postMethod", operation.operationId);
            }
            operationCounter++;
        }
        return objs;
    }
}
