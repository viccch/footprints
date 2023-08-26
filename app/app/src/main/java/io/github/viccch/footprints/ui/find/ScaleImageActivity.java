package io.github.viccch.footprints.ui.find;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import io.github.viccch.footprints.R;

public class ScaleImageActivity extends AppCompatActivity implements View.OnTouchListener {

    public static String M_ARG_PARAM_URL = "url";

    private ImageView imageView;

    private int MODE;//当前状态
    public static final int MODE_NONE = 0;//无操作
    public static final int MODE_DRAG = 1;//单指操作
    public static final int MODE_SCALE = 2;//双指操作

    private Matrix startMatrix = new Matrix();//初始矩阵
    private Matrix endMatrix = new Matrix();//变化后的矩阵
    private PointF startPointF = new PointF();//初始坐标
    private float distance;//初始距离
    private float scaleMultiple;//缩放倍数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_image);

        setTitle("预览图片");
        imageView = findViewById(R.id.imageView);
        imageView.setOnTouchListener(this);

        //设置图片资源
        Glide.with(this)
                .load(getIntent().getStringExtra(M_ARG_PARAM_URL))
                .placeholder(R.drawable.baseline_broken_image_24)
                .into(imageView);
        //设置ImageView的缩放类型
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN://单指触碰

                //起始矩阵先获取ImageView的当前状态
                startMatrix.set(imageView.getImageMatrix());
                //获取起始坐标
                startPointF.set(event.getX(), event.getY());
                //此时状态是单指操作
                MODE = MODE_DRAG;

                break;
            case MotionEvent.ACTION_POINTER_DOWN://双指触碰

                //最后的状态传给起始状态
                startMatrix.set(endMatrix);
                //获取距离
                distance = getDistance(event);
                //状态改为双指操作
                MODE = MODE_SCALE;

                break;
            case MotionEvent.ACTION_MOVE://滑动（单+双）
                if (MODE == MODE_DRAG) {//单指滑动时
                    //获取初始矩阵
                    endMatrix.set(startMatrix);
                    //向矩阵传入位移距离
                    endMatrix.postTranslate(event.getX() - startPointF.x, event.getY() - startPointF.y);
                } else if (MODE == MODE_SCALE) {//双指滑动时
                    //计算缩放倍数
                    scaleMultiple = getDistance(event) / distance;
                    //获取初始矩阵
                    endMatrix.set(startMatrix);
                    //向矩阵传入缩放倍数
                    endMatrix.postScale(scaleMultiple, scaleMultiple, startPointF.x, startPointF.y);
                }
                break;
            case MotionEvent.ACTION_UP://单指离开
            case MotionEvent.ACTION_POINTER_UP://双指离开
                //手指离开后，重置状态
                MODE = MODE_NONE;
                break;
        }
        //事件结束后，把矩阵的变化同步到ImageView上
        imageView.setImageMatrix(endMatrix);
        return true;
    }

    //获取距离
    private static float getDistance(MotionEvent event) {//获取两点间距离
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

}
