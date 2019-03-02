require_relative 'maze.rb'

# Main method class
# Runs part1.1 and part1.2
# Functions temporarily redirect stdout outputs to files

class Main

  MAZES_FILES = ['mediumMaze.txt', 'bigMaze.txt', 'openMaze.txt']
  SEARCH_FILES = ['tinySearch.txt', 'smallSearch.txt', 'mediumSearch.txt']
  DIVIDER = '=============================================================================================='


  def initialize

    run_part1_1
    run_part1_2

  end


  # Runs Part1.1
  # Output redirected to mp1/lib/graph/part1-1.txt
  def run_part1_1

    begin
      old_stdout = $stdout
      $stdout.reopen('part1-1.txt', 'w')

      MAZES_FILES.each do |file_path|
        m = Maze.new("../../data/#{file_path}")
        puts "#{file_path} #{DIVIDER}"
        goal = m.goals[0]
        solns = [:D, :B, :G, :A].map{|type| m.solve_XFS(type, m.start, goal, [])}
        solns.each do |s|
          puts "USING #{Maze::SEARCH_TYPES[s[:type]]}"
          m.construct_soln(s, [goal])
          print "\n"
        end
      end

    ensure
      $stdout = old_stdout
    end

  end




  # Runs Part1.2
  # Output redirected to mp1/lib/graph/part1-1.txt
  def run_part1_2

    begin
      old_stdout = $stdout
      $stdout.reopen('part1-2.txt', 'w')

      SEARCH_FILES.each do |file_path|
        m = Maze.new("../../data/#{file_path}")
        puts "#{file_path} #{DIVIDER}"
        m.solve_Amulti
        print "\n"
      end

    ensure
      $stdout = old_stdout
    end
  end

end

Main.new
