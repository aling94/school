# Represents a post entity with a name and body
# Name is validated for profanity
# Body is sanitized of HTML and cleaned for profanity

class Post < ActiveRecord::Base

  has_many :comments

  validates_with NameValidator
  validates :name, presence: true, length: {minimum: 2, maximum: 20},
            format: {with: /\A[a-zA-Z0-9]+\Z/, message: 'should be letters and numbers only'}
  validates :body, presence: true, length: {maximum: 500}
  validates_with BodyValidator
end
