package org.operationlog.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.IOException;
import java.util.Map;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/24
 **/
public class FreemarkerUtils {

    /**
     * 使用SpEL对字符串进行格式化
     *
     * @param str
     * @param params
     * @return
     */
    public static String formatString(String str, Map<String, Object> params, Object result, String module, String errorMsg) throws IOException {
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setVariable("params", params);
        standardEvaluationContext.setVariable("result", result);
        standardEvaluationContext.setVariable("entityName", StringUtils.isNotEmpty(module) ? module : "{#entityName}");
        standardEvaluationContext.setVariable("errorMsg", result);


        ExpressionParser parser = new SpelExpressionParser();
        final Object value = parser.parseExpression(str, new TemplateParserContext("{", "}")).getValue(standardEvaluationContext, Object.class);
        if (null == value) {
            return null;
        } else if (value.getClass().isPrimitive() || CharSequence.class.isAssignableFrom(value.getClass())) {
            return value.toString();
        } else {
            return JsonUtils.toJsonStr(value);
        }
    }
}
