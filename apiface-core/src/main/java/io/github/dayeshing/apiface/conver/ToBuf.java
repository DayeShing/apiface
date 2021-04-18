package io.github.dayeshing.apiface.conver;

import io.github.dayeshing.apiface.core.comment.impl.ClassMark;
import io.github.dayeshing.apiface.core.comment.impl.FieldMark;
import io.github.dayeshing.apiface.core.comment.impl.MethodMark;
import io.github.dayeshing.apiface.core.comment.impl.ParamMark;
import io.github.dayeshing.apiface.core.comment.tag.GeneralTag;
import io.github.dayeshing.apiface.core.util.ObjectUtil;
import io.github.dayeshing.apiface.serializer.*;

import java.util.List;

/**
 * ToBuf的描述
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @since 1.0
 */
public class ToBuf {

    public static byte[] toBuf(List<ClassMark> marks) {
        MarkSerializer.Mark.Builder builder = MarkSerializer.Mark.newBuilder();
        if(ObjectUtil.isNotEmpty(marks)) {
            for (ClassMark mark : marks) {
                builder.addMarks(getClassMark(mark));
            }
        }
        return builder.build().toByteArray();
    }

    protected static ClassMarkSerializer.ClassMark getClassMark(ClassMark mark){
        ClassMarkSerializer.ClassMark.Builder builder = ClassMarkSerializer.ClassMark.newBuilder();
        builder.setName(mark.getName());
        builder.setDesc(mark.getDesc());
        List<GeneralTag> docTags = mark.getDocTags();
        if(ObjectUtil.isNotEmpty(docTags)) {
            for (GeneralTag docTag : docTags) {
                builder.addDocTags(getDocTag(docTag));
            }
        }
        List<FieldMark> fields = mark.getFields();
        if(ObjectUtil.isNotEmpty(fields)){
            for (FieldMark field : fields) {
                builder.addFields(getFieldMark(field));
            }
        }
        List<MethodMark> methods = mark.getMethods();
        if(ObjectUtil.isNotEmpty(methods)) {
            for (MethodMark method : methods) {
                builder.addMethods(getMethodMark(method));
            }
        }
        return builder.build();
    }

    protected static MethodMarkSerializer.MethodMark getMethodMark(MethodMark mark) {
        MethodMarkSerializer.MethodMark.Builder builder = MethodMarkSerializer.MethodMark.newBuilder();
        builder.setName(mark.getName());
        builder.setDesc(mark.getDesc());
        List<GeneralTag> docTags = mark.getDocTags();
        if(ObjectUtil.isNotEmpty(docTags)) {
            for (GeneralTag docTag : docTags) {
                builder.addDocTags(getDocTag(docTag));
            }
        }
        List<ParamMark> params = mark.getParams();
        if(ObjectUtil.isNotEmpty(params)) {
            for (ParamMark param : params) {
                builder.addParams(getParamMark(param));
            }
        }
        return builder.build();
    }

    protected static ParamMarkSerializer.ParamMark getParamMark(ParamMark mark) {
        ParamMarkSerializer.ParamMark.Builder builder = ParamMarkSerializer.ParamMark.newBuilder();
        builder.setName(mark.getName());
        builder.setType(mark.getType());
        return builder.build();
    }

    protected static FieldMarkSerializer.FieldMark getFieldMark(FieldMark mark) {
        FieldMarkSerializer.FieldMark.Builder builder = FieldMarkSerializer.FieldMark.newBuilder();
        builder.setName(mark.getName());
        builder.setDesc(mark.getDesc());
        builder.setType(mark.getType());
        builder.setExample(mark.getExample());
        List<GeneralTag> docTags = mark.getDocTags();
        if(ObjectUtil.isNotEmpty(docTags)) {
            for (GeneralTag docTag : docTags) {
                builder.addDocTags(getDocTag(docTag));
            }
        }
        return builder.build();
    }

    protected static GeneralTagSerializer.GeneralTag getDocTag(GeneralTag docTag) {
        GeneralTagSerializer.GeneralTag.Builder builder = GeneralTagSerializer.GeneralTag.newBuilder();
        builder.setTagName(docTag.getTagName());
        builder.setValue(docTag.getValue());
        return builder.build();
    }

}
