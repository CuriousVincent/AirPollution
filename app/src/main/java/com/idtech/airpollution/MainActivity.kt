package com.idtech.airpollution


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.idtech.airpollution.ui.main.MainFragment
import com.orhanobut.logger.Logger
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val sharedViewModel by viewModel<MainSharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_view_menu_item, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.action_search)
        val searchViewAndroidActionBar: SearchView = searchViewItem.actionView as SearchView
        searchViewAndroidActionBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewAndroidActionBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                sharedViewModel.searchWord.value = newText
                return false
            }
        })
        searchViewAndroidActionBar.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener{
            override fun onViewAttachedToWindow(p0: View?) {
                Logger.d("onViewAttachedToWindow ${p0?.accessibilityClassName}")
                //search click
                sharedViewModel.isSearch.value = true
            }

            override fun onViewDetachedFromWindow(p0: View?) {
                Logger.d("onViewDetachedFromWindow ${p0?.accessibilityClassName}")
                //search close
                sharedViewModel.isSearch.value = false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }



}