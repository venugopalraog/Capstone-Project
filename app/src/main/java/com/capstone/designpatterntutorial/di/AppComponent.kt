package com.capstone.designpatterntutorial.di;

import com.capstone.designpatterntutorial.presenters.FavoritePresenter;
import com.capstone.designpatterntutorial.presenters.HomePresenter;
import com.capstone.designpatterntutorial.views.activities.HomeActivity;
import com.capstone.designpatterntutorial.views.fragments.FavoriteListFragment;
import com.capstone.designpatterntutorial.views.fragments.NavigationMenuFragment;
import com.capstone.designpatterntutorial.views.fragments.categoryfragment.CategoryFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by venugopalraog on 4/27/17.
 */

@Singleton
@Component(modules={BaseModuleApplication.class})
public interface AppComponent {

    void inject(HomeActivity activity);
    void inject(HomePresenter homePresenter);
    void inject(CategoryFragment categoryFragment);
    void inject(NavigationMenuFragment navigationMenuFragment);

    void inject(FavoritePresenter favoritePresenter);

    void inject(FavoriteListFragment favoriteListFragment);

}
