import { createRouter as createRouter, createWebHistory } from 'vue-router'
import { useStore } from 'vuex'

// Import components
import HomeView from '../views/HomeView.vue';
import LoginView from '../views/LoginView.vue';
import LogoutView from '../views/LogoutView.vue';
import RegisterView from '../views/RegisterView.vue';
import PropertyView from '../views/PropertyView.vue';

import AvailableView from '../views/AvailableView.vue';

import OwnerView from '../views/OwnerView.vue';

import TenantView from '../views/TenantView.vue';
import ManagerView from '../views/ManagerView.vue';
import AboutUsView from '../views/AboutUsView.vue';






/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 */
const routes = [
  {
    path: '',
    name: 'home',
    component: HomeView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/logout",
    name: "logout",
    component: LogoutView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/register",
    name: "register",
    component: RegisterView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/property/:id",
    name: "property",
    component: PropertyView,
    meta: {
      requiresAuth: false
    }
  },
  {

    path: '/available',
    name: 'Available',
    component: AvailableView,
    props: route => ({query: route.query.q}),
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/owner",
    name: "owner",
    component: OwnerView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/manager",
    name: "manager",
    component: ManagerView,
    meta: {
      requiresAuth: false
    }
  },{
    path: '/tenant',
    name: 'Tenant',
    component: TenantView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/about',
    name: 'about',
    component: AboutUsView,
    meta: {
      requiresAuth: false
    }
  },

];

// Create the router
const router = createRouter({
  history: createWebHistory(),
  routes: routes
});

router.beforeEach((to) => {

  // Get the Vuex store
  const store = useStore();

  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    return {name: "login"};
  }
  // Otherwise, do nothing and they'll go to their next destination
});

export default router;
