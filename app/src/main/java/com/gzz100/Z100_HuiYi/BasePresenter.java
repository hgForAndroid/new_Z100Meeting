/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gzz100.Z100_HuiYi;
/**
* Presenter接口基类
* @author XieQXiong
* create at 2016/8/22 15:03
*/

public interface BasePresenter {
    //在实现了View接口方法中的Fragment或Activity中的onResume方法中调用
    void start();

}
