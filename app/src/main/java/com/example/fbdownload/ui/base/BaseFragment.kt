package com.example.fbdownload.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fbdownload.BR
import com.example.fbdownload.data.DataManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment(),
    IFragmentCallback {
    var TAG = "BaseFragment"
    @Inject
    lateinit var dataManager: DataManager

    @Inject
    lateinit var factory: ViewModelFactory
    private val disposable = CompositeDisposable()

    lateinit var activity: Activity
    lateinit var navigators: Navigators
    lateinit var viewModel: VM
    lateinit var dataBinding: DB

    fun inject(
        factory: ViewModelFactory,
        dataManager: DataManager,
    ) {
        this.factory = factory
        this.dataManager = dataManager
    }

    private var mIsAttached: Boolean = false

    override fun getContext(): Context {
        return activity
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        TAG = javaClass.simpleName
        mIsAttached = true
        if (context is Navigators) {
            context.fragmentRequestInject(this)
            activity = context as Activity
            navigators = context
            viewModel = ViewModelProvider(this, factory).get(createViewModel())
            viewModel.fragmentCallback = this
            viewModel.navigation = navigators
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return onCreateViewInternal(inflater, container)
    }

    var isFirst = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI(view)
        navigators.onFragmentResumed(this)
        initView()
        bindViewModel()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideSoftKeyboard()
                false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    private fun hideSoftKeyboard() {
        activity.currentFocus?.let {
            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }


    private fun onCreateViewInternal(inflater: LayoutInflater, container: ViewGroup?): View? {
        var rootView: View? = null
        val resId = getResourceLayout()
        if (resId > 0) {
            dataBinding = DataBindingUtil.inflate(
                inflater,
                getResourceLayout(),
                container,
                false
            )
            dataBinding.setVariable(BR.viewModel, viewModel)
            rootView = dataBinding.root
        }

        return rootView
    }

    abstract fun createViewModel(): Class<VM>
    abstract fun getResourceLayout(): Int
    abstract fun initView()
    abstract fun bindViewModel()

    open fun isShowToolbar(): Boolean {
        return true
    }

    open fun isShowBack(): Boolean {
        return true
    }


    open fun showActionBar(): Boolean {
        return true
    }

    fun replaceFragment(
        fragmentId: KClass<*>,
        bundle: Bundle? = null,
        addToBackStack: Boolean = true
    ) {
        navigators.switchFragment(fragmentId, bundle, addToBackStack)
    }


    fun Disposable.addDisposable() {
        disposable.add(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDestroyView()
        disposable.clear()

    }

    override fun onDetach() {
        super.onDetach()
        mIsAttached = false
    }
}