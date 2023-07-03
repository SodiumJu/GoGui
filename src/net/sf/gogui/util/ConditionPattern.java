package net.sf.gogui.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionPattern {

    public interface EnumerateFunc {

        boolean run(Pattern pattern);

    }

    private static Pattern s_pattern = Pattern.compile("`([^`]+)`");
    
    private String m_pattern_str;

    private ExpressionEval m_expeval;
    private ExpressionEval.Node m_node;
    private ArrayList<Pattern> m_patterns;
    
    private String parsePatternString(String pattern_str) {
        Matcher matcher = s_pattern.matcher(pattern_str);
        StringBuffer out = new StringBuffer();
        int count = 0;
        while (matcher.find()) {
            String tag = Integer.toString(count++);
            matcher.appendReplacement(out, tag);
            String regex = matcher.group(1);
            Pattern pattern = Pattern.compile(regex);
            m_patterns.add(pattern);
        }
        matcher.appendTail(out);
        return out.toString();
    }

    public ConditionPattern(String str) {
        m_pattern_str = str;
        m_patterns = new ArrayList<Pattern>();
        String cond_str;
        if (m_pattern_str.contains("`"))
            cond_str = parsePatternString(m_pattern_str);
        else
            cond_str = parsePatternString("`" + m_pattern_str + "`");
        m_expeval = new ExpressionEval(cond_str);
        m_node = m_expeval.parse();
    }

    public ConditionPattern enumeratePatterns(EnumerateFunc enumfunc) {
        for (int i = 0; i < m_patterns.size(); i++) {
            boolean value = enumfunc.run(m_patterns.get(i));
            String name = Integer.toString(i);
            m_expeval.set(name, value);
        }
        return this;
    }

    public boolean eval() {
        return m_node.eval();
    }
    
    public ArrayList<Pattern> getPatterns() {
        return m_patterns;
    }
    
    public String toString() {
        return m_pattern_str;
    }

}
