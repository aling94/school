# Represents a reply to a post.
# Essentially the same as a post, but without any replies.
# Name and body are validated the same way as the post.

class Comment < ActiveRecord::Base

  belongs_to :post

  validates_with NameValidator
  validates :name, presence: true, length: {minimum: 2, maximum: 20},
            format: {with: /\A[a-zA-Z0-9]+\Z/, message: 'should be letters and numbers only'}
  validates :body, presence: true, length: {maximum: 500}
  validates_with BodyValidator
end
