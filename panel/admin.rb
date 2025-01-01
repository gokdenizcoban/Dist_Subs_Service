require 'socket'

class Configuration
  attr_accessor :fault_tolerance_level

  def initialize(file_path)
    File.open(file_path, "r") do |file|
      file.each_line do |line|
        if line.start_with?("fault_tolerance_level")
          @fault_tolerance_level = line.split("=").last.strip.to_i
        end
      end
    end
  end
end

def send_start_command(host, port)
  begin
    socket = TCPSocket.new(host, port)
    socket.puts("STRT")
    puts "Sent STRT command to #{host}:#{port}"
    socket.close
  rescue => e
    puts "Failed to connect to #{host}:#{port} - #{e.message}"
  end
end

# Read configuration
config = Configuration.new("c:/Users/Yavuz/Desktop/java_serverlari/Dist_Subs_Service/panel/dist_subs.conf")
puts "Fault Tolerance Level: #{config.fault_tolerance_level}"

# Send STRT command to all servers
send_start_command("localhost", 5000) # Server1
send_start_command("localhost", 6000) # Server2
send_start_command("localhost", 7000) # Server3
