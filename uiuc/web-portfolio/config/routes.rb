Rails.application.routes.draw do

  # get 'pages/index'
  # root 'pages#index'
  # get 'pages/:id' => 'pages#show', :constraints => {:id => /[^\/]+/}
  # resources :pages
  get 'posts/index'
  root 'posts#index'
  get 'posts/:id' => 'posts#show', :constraints => {:id => /[^\/]+/}
  resources :posts
  resources :comments
end
