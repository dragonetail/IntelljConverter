package postfix;

import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateWithExpressionSelector;
import com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

public class ToDtoTemplate extends PostfixTemplateWithExpressionSelector {

    public ToDtoTemplate() {
        super(
                "todto",
                "set...",
                JavaPostfixTemplatesUtils.selectorTopmost()
        );
    }


    @Override
    protected void expandForChooseExpression(@NotNull PsiElement expression, @NotNull Editor editor) {
        removeExpressionFromEditor(expression, editor);

        final Project project = expression.getProject();
        final TemplateManager manager = TemplateManager.getInstance(project);
        try {
            String className = ((PsiExpression) expression).getType().getCanonicalText();
            PsiClass psiClass = JavaPsiFacade.getInstance(expression.getProject()).findClass(className, expression.getResolveScope());

            PsiField[] fields = psiClass.getFields();
            final String stringTemplate = getSetsText(expression.getText(), fields) + "$END$";
            final Template template = manager.createTemplate("", "", stringTemplate);

            template.setToReformat(true);
            manager.startTemplate(editor, template);
        } catch (Exception e) {
        }
    }

    private String getSetsText(String objName, @NotNull PsiField... paramentFields) {
        StringBuilder builder = new StringBuilder("");
        for (PsiField field : paramentFields) {
            PsiModifierList modifierList = field.getModifierList();
            if (modifierList == null || modifierList.hasModifierProperty(PsiModifier.STATIC) || modifierList.hasModifierProperty(PsiModifier.FINAL) || modifierList.hasModifierProperty(PsiModifier.SYNCHRONIZED)) {
                continue;
            }
            builder.append(objName + ".set" + getFirstUpperCase(field.getName()) + "(model" +
                    buildGetMethod(field) +
                    ");\n");
        }

        builder.append("\nthis.convertCommonToDTO(model, dto);\n");

        return builder.toString();
    }

    @NotNull
    private String buildGetMethod(PsiField field) {
        if (field.getType().equals(PsiType.BOOLEAN)) {
            return ".is" + getFirstUpperCase(field.getName()) + "()";
        }

//        PsiType type = field.getType();
////        PsiClass containingClass = type;
//        Project project = field.getContainingClass().getProject();
//        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
////        PsiClass supportLibFragmentClass = InspectionPsiUtil.createPsiClass(
////                QUALIFIED_NAME_OF_SUPER_CLASS_FOR_SUPPORT_LIBRARY, aClass.getProject());
//
//        PsiClass targetClass = JavaPsiFacade.getInstance(project).findClass(
//                "com.xahi.common.dto.AbstractDTO", scope);
//        boolean isInheritor = type.isAssignableFrom(targetClass.get, true);
//        if (isInheritor) {
//            String name = containingClass.getName();
//            String classNname = name.substring(0, name.length() - 4);
//
//            return getFirstLowerCase(classNname) + "Converter.toDto(model)";
//        }
        return ".get" + getFirstUpperCase(field.getName()) + "()";
    }

    private String getFirstUpperCase(String oldStr) {
        return oldStr.substring(0, 1).toUpperCase() + oldStr.substring(1);
    }

    private String getFirstLowerCase(String oldStr) {
        return oldStr.substring(0, 1).toLowerCase() + oldStr.substring(1);
    }

    private void removeExpressionFromEditor(@NotNull PsiElement expression, @NotNull Editor editor) {
        Document document = editor.getDocument();
        document.deleteString(expression.getTextRange().getStartOffset(), expression.getTextRange().getEndOffset());
    }
}