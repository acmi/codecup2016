/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import java.util.ListResourceBundle;

public class ErrorMessages_es
extends ListResourceBundle {
    public Object[][] getContents() {
        return new Object[][]{{"MULTIPLE_STYLESHEET_ERR", "Hay m\u00e1s de una hoja de estilos definida en el mismo archivo."}, {"TEMPLATE_REDEF_ERR", "La plantilla ''{0}'' ya est\u00e1 definida en esta hoja de estilos."}, {"TEMPLATE_UNDEF_ERR", "La plantilla ''{0}'' no est\u00e1 definida en esta hoja de estilos."}, {"VARIABLE_REDEF_ERR", "La variable ''{0}'' se ha definido varias veces en el mismo \u00e1mbito."}, {"VARIABLE_UNDEF_ERR", "La variable o el par\u00e1metro ''{0}'' no est\u00e1n definidos."}, {"CLASS_NOT_FOUND_ERR", "No se puede encontrar la clase ''{0}''."}, {"METHOD_NOT_FOUND_ERR", "No se puede encontrar el m\u00e9todo externo ''{0}'' (debe ser p\u00fablico)."}, {"ARGUMENT_CONVERSION_ERR", "No se puede convertir el argumento/tipo de devoluci\u00f3n en la llamada al m\u00e9todo ''{0}''"}, {"FILE_NOT_FOUND_ERR", "Archivo o URI ''{0}'' no encontrado."}, {"INVALID_URI_ERR", "URI ''{0}'' no v\u00e1lido."}, {"FILE_ACCESS_ERR", "No se puede abrir el archivo o URI ''{0}''."}, {"MISSING_ROOT_ERR", "Se esperaba el elemento <xsl:stylesheet> o <xsl:transform>."}, {"NAMESPACE_UNDEF_ERR", "El prefijo ''{0}'' del espacio de nombres no est\u00e1 declarado."}, {"FUNCTION_RESOLVE_ERR", "No se puede resolver la llamada a la funci\u00f3n ''{0}''."}, {"NEED_LITERAL_ERR", "El argumento para ''{0}'' debe ser una serie literal."}, {"XPATH_PARSER_ERR", "Error al analizar la expresi\u00f3n ''{0}'' de XPath."}, {"REQUIRED_ATTR_ERR", "Falta el atributo necesario ''{0}''."}, {"ILLEGAL_CHAR_ERR", "Car\u00e1cter ''{0}'' no permitido en expresi\u00f3n de XPath."}, {"ILLEGAL_PI_ERR", "Nombre ''{0}'' no permitido para la instrucci\u00f3n de proceso."}, {"STRAY_ATTRIBUTE_ERR", "Atributo ''{0}'' fuera del elemento."}, {"ILLEGAL_ATTRIBUTE_ERR", "Atributo ''{0}'' no permitido."}, {"CIRCULAR_INCLUDE_ERR", "import/include circular. Hoja de estilos ''{0}'' ya cargada."}, {"RESULT_TREE_SORT_ERR", "Los fragmentos del \u00e1rbol de resultados no se pueden ordenar (elementos <xsl:sort> ignorados). Debe ordenar los nodos al crear el \u00e1rbol de resultados."}, {"SYMBOLS_REDEF_ERR", "El formato decimal ''{0}'' ya est\u00e1 definido."}, {"XSL_VERSION_ERR", "La versi\u00f3n de XSL ''{0}'' no est\u00e1 soportada por XSLTC."}, {"CIRCULAR_VARIABLE_ERR", "Referencia de variable/par\u00e1metro circular en ''{0}''."}, {"ILLEGAL_BINARY_OP_ERR", "Operador desconocido para expresi\u00f3n binaria."}, {"ILLEGAL_ARG_ERR", "Argumento(s) no permitido(s) para llamada a funci\u00f3n."}, {"DOCUMENT_ARG_ERR", "El segundo argumento de la funci\u00f3n document() debe ser un conjunto de nodos."}, {"MISSING_WHEN_ERR", "Se necesita al menos un elemento <xsl:when> en <xsl:choose>."}, {"MULTIPLE_OTHERWISE_ERR", "S\u00f3lo se permite un elemento <xsl:otherwise> en <xsl:choose>."}, {"STRAY_OTHERWISE_ERR", "<xsl:otherwise> s\u00f3lo puede utilizarse dentro de <xsl:choose>."}, {"STRAY_WHEN_ERR", "<xsl:when> s\u00f3lo puede utilizarse dentro de <xsl:choose>."}, {"WHEN_ELEMENT_ERR", "S\u00f3lo est\u00e1n permitidos los elementos <xsl:when> y <xsl:otherwise> en <xsl:choose>."}, {"UNNAMED_ATTRIBSET_ERR", "Falta el atributo 'name' en <xsl:attribute-set>."}, {"ILLEGAL_CHILD_ERR", "Elemento hijo no permitido."}, {"ILLEGAL_ELEM_NAME_ERR", "No puede llamar a un elemento ''{0}''"}, {"ILLEGAL_ATTR_NAME_ERR", "No puede llamar a un atributo ''{0}''"}, {"ILLEGAL_TEXT_NODE_ERR", "Datos de texto fuera del elemento <xsl:stylesheet> de nivel superior."}, {"SAX_PARSER_CONFIG_ERR", "Analizador JAXP no configurado correctamente"}, {"INTERNAL_ERR", "Error interno de XSLTC irrecuperable: ''{0}''"}, {"UNSUPPORTED_XSL_ERR", "Elemento XSL ''{0}'' no soportado."}, {"UNSUPPORTED_EXT_ERR", "Extensi\u00f3n XSLTC ''{0}'' no reconocida."}, {"MISSING_XSLT_URI_ERR", "El documento de entrada no es una hoja de estilos (el espacio de nombres XSL no est\u00e1 declarado en el elemento ra\u00edz)."}, {"MISSING_XSLT_TARGET_ERR", "No se ha podido encontrar el destino de la hoja de estilos ''{0}''."}, {"NOT_IMPLEMENTED_ERR", "No implementado: ''{0}''."}, {"NOT_STYLESHEET_ERR", "El documento de entrada no contiene una hoja de estilos XSL."}, {"ELEMENT_PARSE_ERR", "No se ha podido analizar el elemento ''{0}''"}, {"KEY_USE_ATTR_ERR", "El atributo use de <key> debe ser node, node-set, string o number."}, {"OUTPUT_VERSION_ERR", "La versi\u00f3n del documento XML de salida deber\u00eda ser 1.0"}, {"ILLEGAL_RELAT_OP_ERR", "Operador desconocido para expresi\u00f3n relacional"}, {"ATTRIBSET_UNDEF_ERR", "Intento de utilizar un conjunto de atributos no existente ''{0}''."}, {"ATTR_VAL_TEMPLATE_ERR", "No se puede analizar la plantilla de valor del atributo ''{0}''."}, {"UNKNOWN_SIG_TYPE_ERR", "Tipo de datos desconocido en la firma de la clase ''{0}''."}, {"DATA_CONVERSION_ERR", "No se puede convertir el tipo de datos ''{0}'' a ''{1}''."}, {"NO_TRANSLET_CLASS_ERR", "Templates no contiene una definici\u00f3n de clase translet v\u00e1lida."}, {"NO_MAIN_TRANSLET_ERR", "Templates no contiene una clase con el nombre ''{0}''."}, {"TRANSLET_CLASS_ERR", "No se ha podido cargar la clase translet ''{0}''."}, {"TRANSLET_OBJECT_ERR", "Clase translet cargada, pero no es posible crear una instancia translet."}, {"ERROR_LISTENER_NULL_ERR", "Intento de establecer ErrorListener para ''{0}'' en null"}, {"JAXP_UNKNOWN_SOURCE_ERR", "S\u00f3lo StreamSource, SAXSource y DOMSource est\u00e1n soportadas por XSLTC"}, {"JAXP_NO_SOURCE_ERR", "El objeto Source pasado a ''{0}'' no tiene contenido."}, {"JAXP_COMPILE_ERR", "No se ha podido compilar la hoja de estilos"}, {"JAXP_INVALID_ATTR_ERR", "TransformerFactory no reconoce el atributo ''{0}''."}, {"JAXP_SET_RESULT_ERR", "setResult() debe llamarse antes de startDocument()."}, {"JAXP_NO_TRANSLET_ERR", "Transformer no tiene un objeto translet encapsulado."}, {"JAXP_NO_HANDLER_ERR", "No se ha definido un manejador de salida para el resultado de la transformaci\u00f3n."}, {"JAXP_NO_RESULT_ERR", "El objeto Result pasado a ''{0}'' no es v\u00e1lido."}, {"JAXP_UNKNOWN_PROP_ERR", "Intento de acceder a una propiedad de Transformer ''{0}'' no v\u00e1lida."}, {"SAX2DOM_ADAPTER_ERR", "No se ha podido crear adaptador SAX2DOMr: ''{0}''."}, {"XSLTC_SOURCE_ERR", "XSLTCSource.build() llamado sin establecer systemId."}, {"ER_RESULT_NULL", "El resultado no deber\u00eda ser nulo"}, {"JAXP_INVALID_SET_PARAM_VALUE", "El valor del par\u00e1metro {0} debe ser un objeto Java v\u00e1lido"}, {"COMPILE_STDIN_ERR", "La opci\u00f3n -i debe utilizarse con la opci\u00f3n -o."}, {"COMPILE_USAGE_STR", "SINOPSIS\n   java org.apache.xalan.xsltc.cmdline.Compile [-o <salida>]\n      [-d <directory>] [-j <archivojar>] [-p <paquete>]\n      [-n] [-x] [-u] [-v] [-h] { <stylesheet> | -i }\n\nOPCIONES\n   -o <salida>    asigna el nombre <salida> al translet\n                  generado.  De forma predeterminada, el nombre del translet\n                  se deriva del nombre de la <hojaestilos>. Esta opci\u00f3n\n                  se ignora si se compilan varias hojas de estilos.\n   -d <directorio> especificar un directorio de destino para el translet\n   -j <archivojar>   empaqueta las clases translet en el archivo jar del\n                  nombre especificado por <archivojar>\n   -p <paquete>   especifica un prefijo de nombre de paquete para todas las\n                  clases translet generadas.\n   -n             habilita la inclusi\u00f3n en l\u00ednea de plantillas (comportamiento predeterminado\n                  mejor seg\u00fan promedio).\n   -x             activa la salida de mensajes de depuraci\u00f3n adicionales\n   -u             interpreta los argumentos <stylesheet>  como URL\n   -i             fuerza que el compilador lea la hoja de estilos de stdin\n   -v             imprime la versi\u00f3n del compilador\n   -h             imprime esta sentencia de uso\n"}, {"TRANSFORM_USAGE_STR", "SINOPSIS \n   java org.apache.xalan.xsltc.cmdline.Transform [-j <archivojar>]\n      [-x] [-n <iteraciones>] {-u <url_documento | <documento>}\n      <clase> [<param1>=<valor1> ...]\n\n   utiliza la <clase> translet para transformar  un documento XML \n   especificado como <documento>. La <clase> translet est\u00e1 en\n   la CLASSPATH del usuario o en el <archivojar> especificado opcionalmente.\nOPCIONES\n   -j <archivojar>    especifica un archivo jar desde el que se va a cargar el translet\n   -x           activa la salida de mensajes de depuraci\u00f3n adicionales\n   -n <iteraciones> ejecuta la transformaci\u00f3n <iteraciones> veces y\n       muestra informaci\u00f3n de perfiles\n   -u <url_documento> especifica el documento de entrada XML como un URL\n"}, {"STRAY_SORT_ERR", "<xsl:sort> s\u00f3lo puede utilizarse dentro de <xsl:for-each> o <xsl:apply-templates>."}, {"UNSUPPORTED_ENCODING", "La codificaci\u00f3n de salida ''{0}'' no est\u00e1 soportada en esta JVM."}, {"SYNTAX_ERR", "Error de sintaxis en ''{0}''."}, {"CONSTRUCTOR_NOT_FOUND", "No se puede encontrar el constructor externo ''{0}''."}, {"NO_JAVA_FUNCT_THIS_REF", "El primer argumento de la funci\u00f3n Java no est\u00e1tica ''{0}'' no es una referencia de objeto v\u00e1lida."}, {"TYPE_CHECK_ERR", "Error al comprobar el tipo de la expresi\u00f3n ''{0}''."}, {"TYPE_CHECK_UNK_LOC_ERR", "Error al comprobar el tipo de una expresi\u00f3n en una ubicaci\u00f3n desconocida."}, {"ILLEGAL_CMDLINE_OPTION_ERR", "La opci\u00f3n ''{0}'' de la l\u00ednea de mandatos no es v\u00e1lida."}, {"CMDLINE_OPT_MISSING_ARG_ERR", "Falta un argumento necesario en la opci\u00f3n ''{0}'' de la l\u00ednea de mandatos."}, {"WARNING_PLUS_WRAPPED_MSG", "AVISO:  ''{0}''\n       :{1}"}, {"WARNING_MSG", "AVISO:  ''{0}''"}, {"FATAL_ERR_PLUS_WRAPPED_MSG", "ERROR MUY GRAVE:  ''{0}''\n           :{1}"}, {"FATAL_ERR_MSG", "ERROR MUY GRAVE:  ''{0}''"}, {"ERROR_PLUS_WRAPPED_MSG", "ERROR:  ''{0}''\n     :{1}"}, {"ERROR_MSG", "ERROR:  ''{0}''"}, {"TRANSFORM_WITH_TRANSLET_STR", "Transformaci\u00f3n con translet ''{0}'' "}, {"TRANSFORM_WITH_JAR_STR", "Transformaci\u00f3n con translet ''{0}'' del archivo jar ''{1}''"}, {"COULD_NOT_CREATE_TRANS_FACT", "No se ha podido crear una instancia de la clase TransformerFactory ''{0}''."}, {"TRANSLET_NAME_JAVA_CONFLICT", "No se ha podido utilizar el nombre ''{0}'' como nombre de la clase translet porque contiene caracteres que no est\u00e1n permitidos en los nombres de clases Java. Se utiliza el nombre ''{1}'' en su lugar."}, {"COMPILER_ERROR_KEY", "Errores del compilador:"}, {"COMPILER_WARNING_KEY", "Avisos del compilador:"}, {"RUNTIME_ERROR_KEY", "Errores de translet:"}, {"INVALID_QNAME_ERR", "Un atributo cuyo valor debe ser un QName o una lista de QNames separados por espacios tiene el valor ''{0}''"}, {"INVALID_NCNAME_ERR", "Un atributo cuyo valor debe ser un NCName tiene el valor ''{0}''"}, {"INVALID_METHOD_IN_OUTPUT", "El atributo method de un elemento <xsl:output> tiene el valor ''{0}''. El valor debe ser ''xml'', ''html'', ''text'' o qname-but-not-ncname"}, {"JAXP_GET_FEATURE_NULL_NAME", "El nombre de caracter\u00edstica no puede ser null en TransformerFactory.getFeature(nombre de tipo String)."}, {"JAXP_SET_FEATURE_NULL_NAME", "El nombre de caracter\u00edstica no puede ser null en TransformerFactory.setFeature(nombre de tipo String, valor booleano)."}, {"JAXP_UNSUPPORTED_FEATURE", "No se puede establecer la caracter\u00edstica ''{0}'' en esta TransformerFactory."}};
    }
}

