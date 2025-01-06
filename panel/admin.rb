require 'socket'
require 'google/protobuf'
require_relative 'configuration_pb'

class AdminClient
  CONFIG_FILE = 'dist_subs.conf'
  

  def initialize(ports)
    @ports = ports
  end

  def read_config
    File.open(CONFIG_FILE, 'r') do |file|
      file.each_line do |line|
        if line.strip.start_with?('fault_tolerance_level')
          return line.split('=').last.strip.to_i
        end
      end
    end
  end

  def send_configuration
    fault_tolerance = read_config
    config = Configuration::Configuration.new(fault_tolerance_level: fault_tolerance)


    @ports.each do |port|
      begin
        socket = TCPSocket.new('localhost', port)
        socket.write(config.to_proto)
        socket.close
        puts "Sent configuration to port #{port} with fault_tolerance_level=#{fault_tolerance}"
      rescue => e
        puts "Failed to connect to port #{port}: #{e.message}"
      end
    end
  end
end

# Example Usage
ports = [7001, 7002, 7003]
admin_client = AdminClient.new(ports)
admin_client.send_configuration
