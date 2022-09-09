package net.orcinus.goodending.client.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;
import net.orcinus.goodending.entities.MarshEntity;

import static net.minecraft.util.math.MathHelper.clamp;
import static net.minecraft.util.math.MathHelper.cos;

@SuppressWarnings("FieldCanBeLocal, unused")
@Environment(EnvType.CLIENT)
public class MarshEntityModel<T extends MarshEntity> extends SinglePartEntityModel<T> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart leftFin;
    private final ModelPart rightFin;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public MarshEntityModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.tail = this.body.getChild("tail");
        this.leftFin = this.body.getChild("leftFin");
        this.rightFin = this.body.getChild("rightFin");
        this.leftLeg = this.root.getChild("leftLeg");
        this.rightLeg = this.root.getChild("rightLeg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        ModelPartData body = root.addChild(
                "body",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F)
                        .uv(30, 0)
                        .cuboid(5.0F, -8.0F, -1.0F, 1.0F, 3.0F, 3.0F)
                        .uv(30, 0)
                        .mirrored(true)
                        .cuboid(-6.0F, -8.0F, -1.0F, 1.0F, 3.0F, 3.0F),
                ModelTransform.of(0.0F, 19.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData tail = body.addChild(
                "tail",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 4.0F),
                ModelTransform.of(0.0F, -4.0F, 5.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData leftFin = body.addChild(
                "leftFin",
                ModelPartBuilder.create()
                        .uv(0, 14)
                        .mirrored(true)
                        .cuboid(0.0F, 0.0F, 0.0F, 0.0F, 6.0F, 6.0F),
                ModelTransform.of(5.0F, -3.0F, 2.0F, -0.7854F, 0.0F, -0.7854F)
        );

        ModelPartData rightFin = body.addChild(
                "rightFin",
                ModelPartBuilder.create()
                        .uv(0, 14)
                        .cuboid(0.0F, 0.0F, 0.0F, 0.0F, 6.0F, 6.0F),
                ModelTransform.of(-5.0F, -3.0F, 2.0F, -0.7854F, 0.0F, 0.7854F)
        );

        ModelPartData leftLeg = root.addChild(
                "leftLeg",
                ModelPartBuilder.create()
                        .uv(34, 23)
                        .cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 5.0F, 4.0F),
                ModelTransform.of(2.5F, 19.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData rightLeg = root.addChild(
                "rightLeg",
                ModelPartBuilder.create()
                        .uv(34, 23)
                        .mirrored(true)
                        .cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 5.0F, 4.0F),
                ModelTransform.of(-2.5F, 19.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );

        return TexturedModelData.of(data, 48, 32);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float limb = clamp(limbDistance, -0.45F, 0.45F);
        float tilt = Math.min(limbDistance / 0.3f, 1.0f);
        float speed = 1f;
        float degree = 1.0f;

        leftFin.pitch = -0.7854F + tilt;
        rightFin.pitch = -0.7854F + tilt;
        body.pitch = tilt * 0.2f;

        rightFin.roll = MathHelper.cos(animationProgress * speed * 0.3F + (float)Math.PI) * degree * 0.2F * 0.5F + 0.7854F;
        leftFin.roll = MathHelper.cos(animationProgress * speed * 0.3F) * degree * 0.2F * 0.5F - 0.7854F;

        rightLeg.pitch = cos(limbAngle * speed * 0.6F) * 1.4F * limb;
        leftLeg.pitch = cos(limbAngle * speed * 0.6F + (float)Math.PI) * 1.4F * limb;
        tail.yaw = cos(limbAngle * speed * 0.6F + (float)Math.PI / 2) * 1.4F * limb;
        body.roll = cos(limbAngle * speed * 0.3F) * 0.35F * limb;
        body.pivotY = cos(limbAngle * speed * 1.2F + (float)Math.PI / 2) * limb + 19F;

    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }


}