# Migration for posts. Defines the schema for a Post
# posts (proj string, name string, body text)

class CreatePosts < ActiveRecord::Migration

  def change
    create_table :posts do |t|
      t.string :proj
      t.string :name
      t.text   :body

      t.timestamps null: false
    end
  end
end
