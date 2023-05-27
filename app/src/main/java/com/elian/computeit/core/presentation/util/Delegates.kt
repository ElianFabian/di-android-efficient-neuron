package com.elian.computeit.core.presentation.util

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// From: https://gist.github.com/gmk57/aefa53e9736d4d4fb2284596fb62710d

/** Activity binding delegate, may be used since onCreate up to onDestroy (inclusive) */
inline fun <T : ViewBinding> AppCompatActivity.viewBinding(crossinline factory: (LayoutInflater) -> T) =
	lazy(LazyThreadSafetyMode.NONE) {
		factory(layoutInflater)
	}

/** Fragment binding delegate, may be used since onViewCreated up to onDestroyView (inclusive) */
fun <T : ViewBinding> Fragment.viewBinding(factory: (View) -> T): ReadOnlyProperty<Fragment, T> =
	object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {
		private var binding: T? = null

		override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
			binding ?: factory(requireView()).also {
				// if binding is accessed after Lifecycle is DESTROYED, create new instance, but don't cache it
				if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
					viewLifecycleOwner.lifecycle.addObserver(this)
					binding = it
				}
			}

		override fun onDestroy(owner: LifecycleOwner) {
			binding = null
		}
	}