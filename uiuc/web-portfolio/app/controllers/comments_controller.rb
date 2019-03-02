# Comments controller. Called when replying to a post. Will redirect back to project page.

class CommentsController < ApplicationController

  # Creates a new reply
  def create
    asn = params[:proj]
    @commentable = Post.find(params[:postid])
    @comment = @commentable.comments.create(params[:comment].permit(:name, :body))
    redirect_to controller: 'posts', action: 'show', id: asn
  end

end