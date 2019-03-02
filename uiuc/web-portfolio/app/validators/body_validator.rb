# Custom validator. Validates and cleans the body attribute for HTML and profanity. Will edit the field in place.

class BodyValidator < ActiveModel::Validator

  def validate(record)
    return unless record.body
    clean_content(record)
  end

  private
  def clean_content(record)
    record.body = Filter.filter_profanity(record.body)
    record.body = ActionController::Base.helpers.sanitize(record.body)
  end
end