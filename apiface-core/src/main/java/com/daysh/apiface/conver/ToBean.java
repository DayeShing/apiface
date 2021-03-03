package com.daysh.apiface.conver;

import com.daysh.apiface.core.comment.impl.ClassMark;
import com.daysh.apiface.core.comment.impl.FieldMark;
import com.daysh.apiface.core.comment.impl.MethodMark;
import com.daysh.apiface.core.comment.impl.ParamMark;
import com.daysh.apiface.core.comment.tag.GeneralTag;
import com.daysh.apiface.serializer.*;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * ToBean的描述
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @since 1.0
 */
public class ToBean {

    public static List<ClassMark> toBeans(byte[] buf)  {
        List<ClassMark> marks = new ArrayList<>(100);
        try {
            MarkSerializer.Mark mark = MarkSerializer.Mark.parseFrom(buf);
            List<ClassMarkSerializer.ClassMark> marksList = mark.getMarksList();
            for (ClassMarkSerializer.ClassMark classMark : marksList) {
                marks.add(getClassMark(classMark));
            }
        }catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return marks;
    }

    protected static ClassMark getClassMark(ClassMarkSerializer.ClassMark classMark) {
        ClassMark mark = new ClassMark();
        mark.setName(classMark.getName());
        mark.setDesc(classMark.getDesc());
        List<GeneralTag> docTags = new ArrayList<> (15);
        List<GeneralTagSerializer.GeneralTag> docTagsList = classMark.getDocTagsList();
        for (GeneralTagSerializer.GeneralTag docTag : docTagsList) {
            docTags.add(getDocTag(docTag));
        }
        mark.setDocTags(docTags);
        List<FieldMark> fields = new ArrayList<> (25);
        List<FieldMarkSerializer.FieldMark> fieldsList = classMark.getFieldsList();
        for (FieldMarkSerializer.FieldMark field : fieldsList) {
            fields.add(getFieldMark(field));
        }
        mark.setFields(fields);
        List<MethodMark> methods = new ArrayList<> (25);
        List<MethodMarkSerializer.MethodMark> methodsList = classMark.getMethodsList();
        for (MethodMarkSerializer.MethodMark method : methodsList) {
            methods.add(getMethodMark(method));
        }
        mark.setMethods(methods);
        return mark;
    }

    protected static MethodMark getMethodMark(MethodMarkSerializer.MethodMark method) {
        MethodMark mark = new MethodMark();
        mark.setName(method.getName());
        mark.setDesc(method.getDesc());

        List<GeneralTag> docTags = new ArrayList<> (15);
        List<GeneralTagSerializer.GeneralTag> docTagsList = method.getDocTagsList();
        for (GeneralTagSerializer.GeneralTag docTag : docTagsList) {
            docTags.add(getDocTag(docTag));
        }
        mark.setDocTags(docTags);
        List<ParamMark> params = new ArrayList<> (15);
        List<ParamMarkSerializer.ParamMark> paramsList = method.getParamsList();
        for (ParamMarkSerializer.ParamMark param : paramsList) {
            params.add(getParamMark(param));
        }
        mark.setParams(params);
        return mark;
    }

    protected static ParamMark getParamMark(ParamMarkSerializer.ParamMark param) {
        ParamMark mark = new ParamMark();
        mark.setName(param.getName());
        mark.setType(param.getType());
        return mark;
    }

    protected static FieldMark getFieldMark(FieldMarkSerializer.FieldMark field) {
        FieldMark mark = new FieldMark();
        mark.setName(field.getName());
        mark.setDesc(field.getDesc());
        mark.setType(field.getType());
        mark.setExample(field.getExample());
        List<GeneralTag> docTags = new ArrayList<> (15);
        List<GeneralTagSerializer.GeneralTag> docTagsList = field.getDocTagsList();
        for (GeneralTagSerializer.GeneralTag docTag : docTagsList) {
            docTags.add(getDocTag(docTag));
        }
        mark.setDocTags(docTags);
        return mark;
    }

    protected static GeneralTag getDocTag(GeneralTagSerializer.GeneralTag docTag) {
        GeneralTag mark = new GeneralTag();
        mark.setTagName(docTag.getTagName());
        mark.setValue(docTag.getValue());
        return mark;
    }


}
