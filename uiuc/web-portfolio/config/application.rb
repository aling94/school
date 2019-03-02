require File.expand_path('../boot', __FILE__)

require 'rails/all'
require_relative '../app/models/projects/projects'
require_relative '../app/models/projects/dir_item'
require_relative '../app/models/projects/file_item'

# Require the gems listed in Gemfile, including any gems
# you've limited to :test, :development, or :production.
Bundler.require(*Rails.groups)

module Assignment30
  class Application < Rails::Application
    # Settings in config/environments/* take precedence over those specified here.
    # Application configuration should go into files in config/initializers
    # -- all .rb files in that directory are automatically loaded.

    # Set Time.zone default to the specified zone and make Active Record auto-convert to this zone.
    # Run "rake -D time" for a list of tasks for finding time zone names. Default is UTC.
    # config.time_zone = 'Central Time (US & Canada)'

    # The default locale is :en and all translations from config/locales/*.rb,yml are auto loaded.
    # config.i18n.load_path += Dir[Rails.root.join('my', 'locales', '*.{rb,yml}').to_s]
    # config.i18n.default_locale = :de

    # Do not swallow errors in after_commit/after_rollback callbacks.
    config.active_record.raise_in_transactional_callbacks = true

    # Load the SVN data and store them in the environment config
    svn_ls = File.open('app/models/projects/svn_list.xml') { |f| Nokogiri::XML(f) }
    svn_lg = File.open('app/models/projects/svn_log.xml') { |f| Nokogiri::XML(f) }
    desc = {
        "Assignment0"   => "The first week of Hel ... CS242. Showcase some code, refactoring exercises, SVN exercises.",
        "Assignment1.0" => "The first week of the Chess assignment. I had to make a Chess engine for this week.",
        "Assignment1.1" => "Second week of Chess. This involved making a static GUI and custom pieces.",
        "Assignment1.2" => "Last week of Chess. Added interactions to the GUI and more visualizations of the board state.",
        "Assignment2.0" => "First week of the Graph assignment. Designed a graph library to represent a flight network.",
        "Assignment2.1" => "Second week of the Graph. Added more options to the console interface with editing features.",
        "Assignment3.0" => "First week of the web portfolio, which you're looking at right now o3o."
    }
    config.dirs, config.files = Projects.project_tree(svn_ls, svn_lg, desc)
  end
end
