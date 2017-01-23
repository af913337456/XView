package am.lghcustomview.ball;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;

import am.lghcustomview.base.BaseItem;
import am.lghcustomview.base.ShowView;

/**
 * Created by LinGuanHong on 2016/1/16.
 */

public class BallView extends ShowView<BaseItem> {

    public BallView(Context context) {
        super(context);
    }

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int hitCounter = 0;
    @Override
    public void beforeLogicLoop() {
        for( int i1=0; i1<size-1; i1++) {
            for( int i2=i1+1; i2<size; i2++) {
                /** 检查双球碰撞 */
                if (
                        ((BallItem)itemList.get(i1))
                                .collide((BallItem)itemList.get(i2)))
                {
                    hitCounter++;
                    Log.d("zzzzz"," hit "+hitCounter);
                    collisionResolve(
                            (BallItem)itemList.get(i1),
                            (BallItem)itemList.get(i2)
                    );
                }else{
                    // Log.d("zzzzz"," pass ");
                }
            }
        }
    }

    @Override
    public BaseItem getItem(int width, int height, Resources resources) {
        return new BallItem(width,height,resources);
    }

    @Override
    public int getCount() {
        return 8;
    }


    private boolean isCircleOutOfBounds(
            PointF center, float circleRadius
    ) {
        return !(center.x + circleRadius <= getWidth() &&
                center.x - circleRadius >=0 &&
                center.y + circleRadius <= getHeight() &&
                center.y - circleRadius >= 0);
    }

    /** 还一种方法计算速度，角度，极度不准确，很多 bug
     *
     *  设每个球密度相同
     *  动能守恒     m1v1+m2v2 = m1v1'+m2v2'
     *  机械能守恒   1/2m1v1^2 ....
     *  m = pv,v=πr^2
     *  合速度       vx^2+vy^2 = v合^2
     *  上诉公式推导出碰撞后的速度
     *
     *  */
    public void collisionResolve(
            BallItem b1, BallItem b2
    ) {
        /** 算法引用 : http://www.vobarian.com/collisions/2dcollisions2.pdf */
        BallHelper initialVector = new BallHelper(
                b1.getCenter(),
                b2.getCenter()
        );

        BallHelper initialUnitNormal =
                new BallHelper(initialVector.getXComponent(),
                initialVector.getYComponent());
        BallHelper initialUnitTangent =
                new BallHelper(initialVector.getXComponent(),
                initialVector.getYComponent());

        initialUnitNormal.unitVectorNormalize();
        initialUnitTangent.unitVectorTangent();

        BallHelper velocityVector1 = new BallHelper(
                b1.getVx(),
                b1.getVy()
        );

        BallHelper velocityVector2 = new BallHelper(
                b2.getVx(),
                b2.getVy()
        );

        float dotProductNormal1 =
                BallHelper.GetDotProduct(initialUnitNormal, velocityVector1);
        float dotProductTangent1 =
                BallHelper.GetDotProduct(initialUnitTangent, velocityVector1);
        float dotProductNormal2 =
                BallHelper.GetDotProduct(initialUnitNormal, velocityVector2);
        float dotProductTangent2 =
                BallHelper.GetDotProduct(initialUnitTangent, velocityVector2);

        float massCircle1 = (float) Math.pow(b1.getRadius(), 2);
        float massCircle2 = (float) Math.pow(b2.getRadius(), 2);

        float sumOfMasses = massCircle1 + massCircle2;
        float differenceOfMasses = massCircle1 - massCircle2;
        float finalNormalVelocity1 = ((dotProductNormal1 * differenceOfMasses) +
                (2 * massCircle2 * dotProductNormal2)) / sumOfMasses;
        float finalNormalVelocity2 = ((dotProductNormal2 * -1 * differenceOfMasses) +
                (2 * massCircle1 * dotProductNormal1)) / sumOfMasses;

        BallHelper finalNormalVelocityVector1 = new BallHelper(
                initialUnitNormal.getXComponent(),
                initialUnitNormal.getYComponent());
        BallHelper finalNormalVelocityVector2 = new BallHelper(
                initialUnitNormal.getXComponent(),
                initialUnitNormal.getYComponent());
        BallHelper finalTangentVelocityVector1 = new BallHelper(
                initialUnitTangent.getXComponent(),
                initialUnitTangent.getYComponent());
        BallHelper finalTangentVelocityVector2 = new BallHelper(
                initialUnitTangent.getXComponent(),
                initialUnitTangent.getYComponent());

        // 计算比例
        finalNormalVelocityVector1.MultiplyScalar(finalNormalVelocity1);
        finalNormalVelocityVector2.MultiplyScalar(finalNormalVelocity2);
        finalTangentVelocityVector1.MultiplyScalar(dotProductTangent1);
        finalTangentVelocityVector2.MultiplyScalar(dotProductTangent2);


        b1.setVx(finalNormalVelocityVector1.getXComponent() +
                finalTangentVelocityVector1.getXComponent());

        b1.setVy(finalNormalVelocityVector1.getYComponent() +
                finalTangentVelocityVector1.getYComponent());

        b2.setVx(finalNormalVelocityVector2.getXComponent() +
                finalTangentVelocityVector2.getXComponent());

        b2.setVy(finalNormalVelocityVector2.getYComponent() +
                finalTangentVelocityVector2.getYComponent());

        /** 计算最小偏移值，防止求纠缠 */
        /** 碰撞时平移，求交点坐标，再求差距，建议画图理解 */
        calculateMinimumTranslationDistance(b1, b2);
    }

    private void calculateMinimumTranslationDistance(
            BallItem circle1Index, BallItem circle2Index) {
        BallHelper positionDifferenceVector = new BallHelper
                (
                        circle2Index.getCenter(),
                        circle1Index.getCenter()
                );
        /** 两球圆心的距离 */
        float distanceBetweenCenters = positionDifferenceVector.GetDistance();
        Log.d("zzzzz","distanceBetweenCenters "+distanceBetweenCenters);
        float radiusSum =  /** 两球的半径之和 */
                circle1Index.getRadius()
                        +
                        circle2Index.getRadius();
        Log.d("zzzzz","radiusSum "+radiusSum);
        if (distanceBetweenCenters != 0) {
            positionDifferenceVector.MultiplyScalar(
                    /** (总 - 实际)/实际，此时属于碰撞进入，radiusSum > distanceBetweenCenters */
                    (radiusSum - distanceBetweenCenters) / distanceBetweenCenters
            );
        }
        Log.d("zzzzz","getXComponent getYComponent "
                +positionDifferenceVector.getXComponent()+"--"
                +
                positionDifferenceVector.getYComponent());

        float translationDistanceX1 = circle1Index.getCenter().x;
        float translationDistanceY1 = circle1Index.getCenter().y;

        float translationDistanceX2 = circle2Index.getCenter().x;
        float translationDistanceY2 = circle2Index.getCenter().y;

        Log.d("zzzzz","getXComponent() / 2 "+positionDifferenceVector.getXComponent() / 2);
        Log.d("zzzzz","getYComponent() / 2 "+positionDifferenceVector.getYComponent() / 2);

        translationDistanceX1 = translationDistanceX1 +
                (positionDifferenceVector.getXComponent() / 2);
        translationDistanceY1 = translationDistanceY1 +
                (positionDifferenceVector.getYComponent() / 2);
        translationDistanceX2 = translationDistanceX2 - /** 减 */
                (positionDifferenceVector.getXComponent() / 2);
        translationDistanceY2 = translationDistanceY2 -
                (positionDifferenceVector.getYComponent() / 2);
        Log.d("zzzzz","translationDistanceX1 "+translationDistanceX1);
        Log.d("zzzzz","translationDistanceY1 "+translationDistanceY1);

        Log.d("zzzzz","translationDistanceX2 "+translationDistanceX2);
        Log.d("zzzzz","translationDistanceY2 "+translationDistanceY2);

        PointF newCenter1 = new PointF(translationDistanceX1, translationDistanceY1);
        PointF newCenter2 = new PointF(translationDistanceX2, translationDistanceY2);

        if (!isCircleOutOfBounds(newCenter1, circle1Index.getRadius())) {
            circle1Index.setCenter(newCenter1);
        }
        if (!isCircleOutOfBounds(newCenter2, circle2Index.getRadius())) {
            circle2Index.setCenter(newCenter2);
        }
    }

}