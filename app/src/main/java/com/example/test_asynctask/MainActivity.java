package com.example.test_asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTv1, mTv2;
    private ProgressBar mPb;
    private Button mBt;
    private boolean flag = true;
    private MyTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv1 = (TextView) findViewById(R.id.tv1);
        mTv2 = (TextView) findViewById(R.id.tv2);
        mPb = (ProgressBar) findViewById(R.id.pb1);
        mBt = (Button) findViewById(R.id.bt1);
        mTv1.setText("点击开始执行AsyncTask");
        //设置ProgressBar,TextView2不可见
        mPb.setVisibility(View.INVISIBLE);
        mTv2.setVisibility(View.INVISIBLE);
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    flag = false;
                    task = new MyTask();
                    //执行MyTask;
                    task.execute();
                }
            }
        });

    }

    @Override
    //Activity销毁时调用的方法
    protected void onDestroy() {
        super.onDestroy();
        flag = true;
        //调用AsyncTask的onCancelled方法
        task.cancel(true);
    }

    class MyTask extends AsyncTask<Void, Integer, String> {

        @Override
        //初始化AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            mTv1.setText("已经开始执行AsyncTask");
            if (flag == false) {
                //设置按钮失去焦点
                mBt.setEnabled(false);
                //设置ProgressBar,TextView2可见
                mPb.setVisibility(View.VISIBLE);
                mTv2.setVisibility(View.VISIBLE);
            }
        }

        @Override
        //将数据通过publishProgress(Progress...values)传输给onPostExectue
        //并且将返回值传给onPostExecute
        protected String doInBackground(Void... voids) {
            for (int i = 1; i <= 100; i++) {
                if (isCancelled()) {
                    return null;
                }
                try {
                    Thread.sleep(50);
                    //这里的变量i将被传输给onProgressUpdate的values[0]中
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "AsyncTask执行完毕";
        }

        @Override
        //接受publishProgress(Progress...values)传递过来的值
        //values是一个数组,一般取值用values[0]
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mTv2.setText("当前执行进度" + values[0]);
        }


        @Override
        //执行完毕时调用的方法
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mPb.setVisibility(View.INVISIBLE);
            mTv2.setVisibility(View.INVISIBLE);
            mTv1.setText(s);
            flag = true;
            mBt.setEnabled(true);
        }

        @Override
        //销毁AsyncTask时调用的方法
        protected void onCancelled() {
            super.onCancelled();
            //销毁Activity时task可能还在执行状态，设置为空
            task = null;
        }
    }
}
