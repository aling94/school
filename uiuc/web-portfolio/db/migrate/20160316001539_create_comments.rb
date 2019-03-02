# Migration for comments. Defines the schema for a Comment
# comments (name string, body text, post_id int)

class CreateComments < ActiveRecord::Migration

  def change
    create_table :comments do |t|
      t.string :name
      t.text   :body
      t.references :post, index: true

      t.timestamps
    end

  end
end
