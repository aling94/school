# Migration for filters. Defines the schema for a Filter
# filters (flag string, sub string)


class CreateFilters < ActiveRecord::Migration

  def change
    create_table :filters do |t|
      t.string :flag
      t.string :sub

      t.timestamps null: false
    end
  end
end
