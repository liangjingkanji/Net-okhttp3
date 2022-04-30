/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.net.sample.ui.fragment

import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentStateLayoutBinding
import com.drake.net.utils.scope


class StateLayoutFragment :
    EngineFragment<FragmentStateLayoutBinding>(R.layout.fragment_state_layout) {

    override fun initData() {
    }

    override fun initView() {
        binding.state.onRefresh {
            scope {
                binding.tvFragment.text = Get<String>("banner/json").await()
            }
        }.showLoading()
    }

}
