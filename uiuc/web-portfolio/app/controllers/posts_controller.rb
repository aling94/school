# Posts controller. Main controller for displaying pages. Pulls both the project trees and the post and comments
# from the database and loads them into the corresponding template.

class PostsController < ApplicationController

  # Home page
  def index
    @tree = Rails.application.config.dirs.select{|path,_| path.split('/').size == 1 }
    @filters = Filter.all.to_a
  end

  # Project page
  def show
    asn = params[:id]
    redirect_to action:'index' unless Rails.application.config.dirs.key?(asn)
    set_proj(asn)
    @post = Post.new
    @comment = Comment.new
  end

  # Creates a new post when a form is submitted. Will redirect back to project page
  def create
    asn = params[:proj]
    params[:post][:proj] = asn
    @post = Post.new(post_params)
    if !Rails.application.config.dirs.key?(asn)
      redirect_to action:'index'
    elsif @post.save
      redirect_to action:'show', id: asn
    else
      set_proj(asn)
      @comment = Comment.new
      render 'show'
    end
  end

  protected
  # Strong parameters helper for making a new post
  def post_params
    params.require(:post).permit(:proj, :name, :body)
  end

  # Sets the instance variables for rendering a page.
  def set_proj(asn)
    @tree = Rails.application.config.dirs[asn]
    @files = Rails.application.config.files.select {|path,_| path.split('/').first == asn}
    @posts = (Post.where(proj: asn).order(created_at: :desc, id: :desc)).to_a
  end
end
