# Custom validation class. Validates the name of a record for profanity.

class NameValidator < ActiveModel::Validator

  def validate(record)
    return unless record.name
    unless record.name == Filter.filter_profanity(record.name)
      record.errors[:name] << 'No profane names please!'
    end
  end
end