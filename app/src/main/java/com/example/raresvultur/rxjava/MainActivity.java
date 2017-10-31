package com.example.raresvultur.rxjava;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Arrays;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Version 1
        Observable<String> myObservable = Observable.from(Arrays.asList("Hello from RxJava",
                "Welcome...", "" +
                        "Goodbye"));

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i("RX", "Rx Java events completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("RX", "Error found processing stream");
            }

            @Override
            public void onNext(String s) {
                Log.i("RX", "New event - " + s);
            }
        };

        myObservable.subscribe(mySubscriber);






        //Version 2
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                //Emitting Numbers
                subscriber.onNext(10);
                subscriber.onNext(3);
                subscriber.onNext(9);
                //Stream completed with success
                subscriber.onCompleted();
            }
        });


        Action1<Integer> onNextAction = new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.i("RX", "New number : " + integer);
            }
        };
        Action1<Throwable> onError = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("RX", "Error: " +throwable.getMessage(), throwable);
            }
        };
        Action0 onComplete = new Action0() {
            @Override
            public void call() {
                Log.i("RX", "Rx number stream completed");
            }
        };

        observable.subscribe(onNextAction, onError, onComplete);







        //Version 3 + some validation
        String content = "This is an example \n " +
                "Looking for lines with the word RxJava\n" +
                "We are finished.";
        Observable
                .just(content)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String content) {
                        return Observable.from(content.split("\n"));
                    }})
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(final String line) {
                        return line.contains("RxJava");
                    }
                })
                .count()
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer s) {
                        Log.i("RX", "Number of Lines " + s);
                    }
                });

    }
}
