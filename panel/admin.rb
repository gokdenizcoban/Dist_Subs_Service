require 'socket'

# Configuration sınıfı
class Configuration
  attr_reader :fault_tolerance_level

  def initialize(fault_tolerance_level)
    @fault_tolerance_level = fault_tolerance_level
  end
end

# Konfigürasyon dosyasını okuma
config_file = "dist_subs.conf"
fault_tolerance_level = 1

if File.exist?(config_file)
  File.foreach(config_file) do |line|
    if line.start_with?("fault_tolerance_level")
      fault_tolerance_level = line.split('=').last.strip.to_i
    end
  end
end

# Configuration nesnesi oluşturma
config = Configuration.new(fault_tolerance_level)
puts "Fault Tolerance Level from Configuration: #{config.fault_tolerance_level}"

# Sunucular listesi
servers = [
  { host: 'localhost', port: 5001 },
  { host: 'localhost', port: 5002 },
  { host: 'localhost', port: 5003 }
]

successful_connections = 0

# Sunuculara bağlan ve komut gönder
servers.each do |server|
  begin
    socket = TCPSocket.new(server[:host], server[:port])
    puts "Connected to #{server[:host]}:#{server[:port]}"

    # STRT komutunu gönder
    socket.puts "STRT"
    response = socket.gets.chomp

    if response == "YEP"
      puts "Server #{server[:host]}:#{server[:port]} started successfully."
      successful_connections += 1
    else
      puts "Server #{server[:host]}:#{server[:port]} failed to start."
    end

    socket.close
  rescue => e
    puts "Error connecting to #{server[:host]}:#{server[:port]} - #{e.message}"
  end
end

# Hata toleransı kontrolü
if successful_connections >= config.fault_tolerance_level
  puts "System operational with #{successful_connections} servers."
else
  puts "System failure: Only #{successful_connections} servers operational, required: #{config.fault_tolerance_level}."
end
