package com.example.fbdownload.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.example.fbdownload.App
import com.example.fbdownload.BR
import com.example.fbdownload.data.DataManager
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity<VM: BaseViewModel, DB: ViewDataBinding> : AppCompatActivity(),
    Navigators {
    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var dataManager: DataManager

    private val mDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    fun inject(
        factory: ViewModelFactory,
        dataManager: DataManager,
    ) {
        this.factory = factory
        this.dataManager = dataManager
    }

    abstract fun createViewModel(): Class<VM>

    abstract fun getContentView(): Int

    abstract fun initAction()

    abstract fun initData()

    lateinit var mViewModel: VM
    lateinit var mDataBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as? App)?.requestInject(this)
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, getContentView())
        mViewModel = ViewModelProviders.of(this, factory).get(createViewModel())
        mDataBinding.setVariable(BR.viewModel, mViewModel)
        initAction()
        initData()

    }

    override fun showActivity(activity: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, activity)
        intent.putExtras(bundle ?: Bundle())
        startActivity(intent)
    }


    override fun fragmentRequestInject(fragment: BaseFragment<*, *>) {
        fragment.inject(factory,dataManager)
    }

}