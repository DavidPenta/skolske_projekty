import socket
import math
import random
import os
import time
import signal

server_socket = None
client_socket = None
server_address = None


# client menu (after CTRL + C)
def client_menu(sig, frm):
    global client_socket
    global server_address
    if isinstance(client_socket, socket.socket) and isinstance(server_address, tuple):
        send(client_socket, server_address, 6, 0, 1, 1, "", 0)
        print("\n1) SPRAVA")
        print("2) SUBOR")
        print("3) Spat do MENU")
        x = input("Vybrana moznost: ")
        if x == "1":
            message = input("Sprava: ")
            send_message(client_socket, server_address, message)
            send(client_socket, server_address, 7, 0, 1, 1, "", 0)

        elif x == "2":
            file_name = input("Nazov suboru: ")
            try:
                file = open(file_name, "rb")
                file.close()
                send_file(client_socket, server_address, file_name)
                send(client_socket, server_address, 7, 0, 1, 1, "", 0)
            except FileNotFoundError:
                print("Subor sa nenasiel")

        elif x == "3":
            type_of_packet = 8
            payload = bytearray(type_of_packet.to_bytes(1, 'big'))
            client_socket.sendto(payload, server_address)
            client_socket.close()
            client_socket = None

        else:
            print("Moznost neexistuje")


# server menu (after CTRL + C)
def server_menu(sig, frm):
    try:
        x = input("\nNaozaj sa chcete vratit do menu? (Y/n)> ")
        if x.lower().startswith('y') or x == "":
            if isinstance(server_socket, socket.socket):
                server_socket.close()
            menu()
        else:
            return

    except KeyboardInterrupt:
        print("EXIT")
        exit(1)


# vypocet crc
def crc16(data):
    crc = 0x0000
    for i in range(0, len(data)):
        crc ^= data[i] << 8
        for j in range(0, 8):
            if (crc & 0x8000) > 0:
                crc = (crc << 1) ^ 0x1021
            else:
                crc = crc << 1
    return crc & 0xFFFF


# odoslanie packetu
def send(sender_socket, send_to_address, type_of_packet, size_of_data, order_of_packet, number_of_packets, message, with_error):
    payload = bytearray(type_of_packet.to_bytes(1, 'big'))

    if type(message) is str:
        encoded_msg = message.encode()
    else:
        encoded_msg = message

    size_of_data = len(message)
    payload.extend(size_of_data.to_bytes(2, 'big'))
    payload.extend(order_of_packet.to_bytes(2, 'big'))
    payload.extend(number_of_packets.to_bytes(2, 'big'))

    # print(encoded_msg)
    payload.extend(encoded_msg)
    byte_array = bytearray(encoded_msg)
    crc = crc16(byte_array)
    payload.extend(crc.to_bytes(2, 'big'))
    # zmenenie bitu v datach
    if with_error == 1:
        if size_of_data > 0:
            if random.uniform(0, 1) < 0.2:
                m = random.randrange(7, 7 + size_of_data)
                b = payload[m]
                s = list(bin(b))
                n = random.randrange(2, len(s))
                if s[n] == "0":
                    s[n] = "1"
                else:
                    s[n] = "0"
                s = "".join(s[2:])
                g = int(s, base=2)
                payload[m] = g
        if size_of_data > 5:
            if random.uniform(0, 1) < 0.15:
                m = random.randrange(7, 7 + size_of_data)
                b = payload[m]
                s = list(bin(b))
                n = random.randrange(2, len(s))
                if s[n] == "0":
                    s[n] = "1"
                else:
                    s[n] = "0"
                s = "".join(s[2:])
                g = int(s, base=2)
                payload[m] = g
        if size_of_data > 20:
            if random.uniform(0, 1) < 0.12:
                m = random.randrange(7, 7 + size_of_data)
                b = payload[m]
                s = list(bin(b))
                n = random.randrange(2, len(s))
                if s[n] == "0":
                    s[n] = "1"
                else:
                    s[n] = "0"
                s = "".join(s[2:])
                g = int(s, base=2)
                payload[m] = g
        if size_of_data > 50:
            if random.uniform(0, 1) < 0.10:
                m = random.randrange(7, 7 + size_of_data)
                b = payload[m]
                s = list(bin(b))
                n = random.randrange(2, len(s))
                if s[n] == "0":
                    s[n] = "1"
                else:
                    s[n] = "0"
                s = "".join(s[2:])
                g = int(s, base=2)
                payload[m] = g

    sender_socket.sendto(payload, send_to_address)


# posielanie spravy
def send_message(client_sock, server_addr, message):
    fragment_size = int(input("Maximalna velkost fragmentu (max 1463): "))
    if fragment_size > 1463 or fragment_size < 1:
        fragment_size = 1463
    print("Maximalna velkost fragmentu je nastavena na:", fragment_size)

    with_error = input("S chybami? (1): ")
    if with_error == "":
        with_error = "1"
    with_error = int(with_error)

    start_of_fragment = 0
    end_of_fragment = fragment_size

    number_of_packets = math.ceil(len(message) / fragment_size)

    print("Pocet fragmentov je:", number_of_packets)

    #  sending first packet of a message
    type_of_packet = 1
    if number_of_packets == 1:
        size_of_data = len(message)
    else:
        size_of_data = fragment_size
    order_of_packet = 1

    send(client_sock, server_addr, type_of_packet, size_of_data, order_of_packet, number_of_packets, message[start_of_fragment:size_of_data], with_error)

    while True:  # waiting for ack
        data, s_address = client_sock.recvfrom(1500)

        if s_address == server_addr and data[0] == 5 and order_of_packet == int.from_bytes(data[3:5], byteorder='big', signed=False) and number_of_packets == int.from_bytes(data[5:7], byteorder='big', signed=False):
            # print("packet bol uspesne prijaty")
            break
        else:  # packet needs to be send again
            send(client_sock, server_addr, type_of_packet, size_of_data, order_of_packet, number_of_packets, message[start_of_fragment:end_of_fragment], with_error)

    if number_of_packets == 1:
        print("Server sprave prijal spravu")
        return

    #  sending middle packets of a message
    if number_of_packets > 2:
        for i in range(number_of_packets - 2):
            type_of_packet = 4
            order_of_packet += 1
            start_of_fragment = end_of_fragment
            end_of_fragment += fragment_size
            send(client_sock, server_addr, type_of_packet, size_of_data, order_of_packet, number_of_packets, message[start_of_fragment:end_of_fragment], with_error)

            while True:  # waiting for ack
                data, s_address = client_sock.recvfrom(1500)

                if s_address == server_addr and data[0] == 5 and order_of_packet == int.from_bytes(data[3:5], byteorder='big', signed=False) and number_of_packets == int.from_bytes(data[5:7], byteorder='big', signed=False):
                    # print("packet bol uspesne prijaty")
                    break
                else:  # packet needs to be send again
                    send(client_sock, server_addr, type_of_packet, size_of_data, order_of_packet, number_of_packets, message[start_of_fragment:end_of_fragment], with_error)

    #  sending last packet of a message
    type_of_packet = 2
    order_of_packet += 1

    start_of_fragment = end_of_fragment
    end_of_fragment = len(message)
    if len(message) % fragment_size > 0:
        size_of_data = len(message) % fragment_size
    else:
        size_of_data = fragment_size

    send(client_sock, server_addr, type_of_packet, size_of_data, order_of_packet, number_of_packets, message[start_of_fragment:end_of_fragment], with_error)

    while True:  # waiting for ack
        data, s_address = client_sock.recvfrom(1500)

        if s_address == server_addr and data[0] == 5 and order_of_packet == int.from_bytes(data[3:5], byteorder='big', signed=False) and number_of_packets == int.from_bytes(data[5:7], byteorder='big', signed=False):
            print("Server sprave prijal spravu")
            break
        else:  # packet needs to be send again
            send(client_sock, server_addr, type_of_packet, size_of_data, order_of_packet, number_of_packets, message[start_of_fragment:end_of_fragment], with_error)


# posielanie suboru
def send_file(client_sock, server_addr, file_name):
    size = os.path.getsize(file_name)
    file = open(file_name, "rb")
    file_size = os.path.getsize(file_name)
    print("Subor:", file_name)
    print("Velkost:", size, "B")
    print("Cesta k suboru:", os.path.abspath(file_name))

    fragment_size = int(input("Maximalna velkost fragmentu (max 1463): "))
    if fragment_size > 1463 or fragment_size < 1:
        fragment_size = 1463

    if file_size / fragment_size > 65355:
        print("Maximalna velkost fragmentu je prilis mala")
        while file_size / fragment_size > 65355:
            fragment_size += 1

    print("Maximalna velkost fragmentu je nastavena na:", fragment_size)

    number_of_name_packets = math.ceil(len(file_name) / fragment_size)
    number_of_packets = math.ceil(file_size / fragment_size)
    number_of_packets += number_of_name_packets

    with_error = input("S chybami? (1): ")
    if with_error == "":
        with_error = "1"
    with_error = int(with_error)

    print("Pocet fragmentov je:", number_of_packets)

    message = file.read()

    start_of_fragment = 0
    end_of_fragment = fragment_size

    order_of_packet = 1

    type_of_packet = 9
    #  sending name of a file
    while order_of_packet <= number_of_name_packets:
        size_of_data = fragment_size
        if order_of_packet == number_of_name_packets:
            if len(file_name) % fragment_size > 0:
                size_of_data = len(file_name) % fragment_size
                end_of_fragment = len(file_name)

        send(client_sock, server_addr, type_of_packet, size_of_data, order_of_packet, number_of_packets, file_name[start_of_fragment:end_of_fragment], with_error)
        while True:  # waiting for ack
            data, s_address = client_sock.recvfrom(1500)
            if s_address == server_addr and data[0] == 5 and order_of_packet == int.from_bytes(data[3:5], byteorder='big', signed=False) and number_of_packets == int.from_bytes(data[5:7], byteorder='big', signed=False):
                break
            else:  # packet needs to be send again
                send(client_sock, server_addr, type_of_packet, size_of_data, order_of_packet, number_of_packets, file_name[start_of_fragment:end_of_fragment], with_error)
        order_of_packet += 1
        start_of_fragment = end_of_fragment
        end_of_fragment += fragment_size

    start_of_fragment = 0
    end_of_fragment = fragment_size

    #  sending first packet with content
    type_of_packet = 10
    size_of_data = fragment_size

    if number_of_packets == 1 + order_of_packet:
        if len(message) % fragment_size > 0:
            size_of_data = len(message) % fragment_size
            end_of_fragment = len(message)

    send(client_sock, server_addr, type_of_packet, len(message[start_of_fragment:end_of_fragment]), order_of_packet, number_of_packets, message[start_of_fragment:end_of_fragment], with_error)

    while True:  # waiting for ack
        data, s_address = client_sock.recvfrom(1500)
        if s_address == server_addr and data[0] == 5 and order_of_packet == int.from_bytes(data[3:5], byteorder='big', signed=False) and number_of_packets == int.from_bytes(data[5:7], byteorder='big', signed=False):
            break
        else:  # packet needs to be send again
            send(client_sock, server_addr, type_of_packet, size_of_data, order_of_packet, number_of_packets, message[start_of_fragment:end_of_fragment], with_error)
    order_of_packet += 1
    start_of_fragment = end_of_fragment
    end_of_fragment += fragment_size

    #  sending rest of content
    type_of_packet = 4

    while order_of_packet <= number_of_packets:
        size_of_data = fragment_size
        if order_of_packet == number_of_packets:
            if len(message) % fragment_size > 0:
                size_of_data = len(message) % fragment_size
                end_of_fragment = len(message)

        send(client_sock, server_addr, type_of_packet, size_of_data, order_of_packet, number_of_packets, message[start_of_fragment:end_of_fragment], with_error)
        while True:  # waiting for ack
            data, s_address = client_sock.recvfrom(1500)
            if s_address == server_addr and data[0] == 5 and order_of_packet == int.from_bytes(data[3:5], byteorder='big', signed=False) and number_of_packets == int.from_bytes(data[5:7], byteorder='big', signed=False):
                break
            else:  # packet needs to be send again
                send(client_sock, server_addr, type_of_packet, size_of_data, order_of_packet, number_of_packets, message[start_of_fragment:end_of_fragment], with_error)
        order_of_packet += 1
        start_of_fragment = end_of_fragment
        end_of_fragment += fragment_size

    print("Server sprave prijal subor")


# client initialization and keepalive packet receiving
def client():
    signal.signal(signal.SIGINT, client_menu)
    global client_socket
    global server_address
    try:
        print("----------------------\n\n       KLIENT\n\n----------------------\n\n")
        client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        address = input("Vlozte IP adresu serveru (127.0.0.1): ")
        if address == "":
            address = '127.0.0.1'

        port = int(input("Port: "))

        while port > 65535 or port < 1025:
            print("Zadajte ine cislo")
            port = int(input("Port: "))

        server_address = (address, int(port))
        try:
            client_socket.sendto(str.encode(""), server_address)
        except socket.error:
            print("Server sa nenasiel")
            return

        client_socket.settimeout(30)
        data, address = client_socket.recvfrom(1500)
        data = data.decode()
        if data == "1":
            print("Pripojene k adrese: " + str(server_address))

        print("Pre zobrazenie klient menu stecte CTRL + C")

        interval = 10

        while client_socket is not None:
            m = bytearray()
            m.append(0)
            client_socket.sendto(m, server_address)

            try:
                client_socket.settimeout(5)
                data = client_socket.recv(1500)
                if data[0] == 0:
                    print("Keepalive funguje")
                    pass

            except socket.timeout:
                print("\nSpojenie bolo prerusene")
                time.sleep(2)
                # exit(0)
                return
            time.sleep(interval)

    except (socket.timeout, socket.gaierror) as e:
        print(e)
        print("Server sa nenasiel")


# server initialization and packet receiving
def server():
    global server_socket
    signal.signal(signal.SIGINT, server_menu)

    print("----------------------\n\n       SERVER\n\n----------------------")
    hostname = socket.gethostname()
    local_ip = socket.gethostbyname(hostname)
    print("hostname:", hostname)
    print("local_ip:", local_ip, "\n")

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    port = int(input("Port: "))
    while port > 65535 or port < 1025:
        print("Zadajte ine cislo")
        port = int(input("Port: "))

    path_ = input("Kam sa budu ukladat prijate subory(C:\\Users\\david\\Desktop\\PKS2- Downloads): ")
    if path_ == "":
        path_ = "C:\\Users\\david\\Desktop\\PKS2- Downloads"

    print("Prijate subory sa budu ukladat do", path_)

    server_socket.bind(("", int(port)))
    print("Caka sa na klienta")

    data, client_address = server_socket.recvfrom(1500)
    server_socket.sendto(str.encode("1"), client_address)
    print("Zacalo so apojenie s kientom na adrese: " + str(client_address))
    server_socket.settimeout(30)
    seq = 1
    msg = ""
    file_name = ""

    print("Pre zobrazenie server menu stecte CTRL + C")

    errors = 0

    # primanie packetov
    while True:
        try:
            data, client_address = server_socket.recvfrom(1500)
        except socket.timeout:
            print("Adresa", client_address, "presala posielat keepalive")
            time.sleep(2)
            return

        if data[0] == 0:  # keepalive
            m = bytearray()
            m.append(0)
            server_socket.sendto(m, client_address)
            print("Keepalive funguje")

        if data[0] == 6:  # keepalive stop
            server_socket.settimeout(None)
            print("Klient pozastavil keepalive")

        if data[0] == 7:  # keepalive restart
            server_socket.settimeout(30)
            print("Klient spustil keepalive")

        if data[0] == 8:  # client ended session
            server_socket.close()
            print("Klient ukoncil spojenie")
            time.sleep(2)
            break

        elif data[0] == 1:  # start of message
            size_of_data = int.from_bytes(data[1:3], byteorder='big', signed=False)
            order_of_packet = int.from_bytes(data[3:5], byteorder='big', signed=False)
            number_of_packets = int.from_bytes(data[5:7], byteorder='big', signed=False)

            if crc16(data[7:]) == 0:  # crc check
                msg += data[7:7+size_of_data].decode(errors='replace')
                send(server_socket, client_address, 5, 0, order_of_packet, number_of_packets, "", 0)
                if number_of_packets == 1:
                    print("Prisla sprava:")
                    print(msg)
                    print("Pocet paketov u ktorych bola najdena chyba: ", errors)
                    errors = 0
                    msg = ""
                    seq = 1
                else:
                    while True:  # rest of message
                        data, client_address = server_socket.recvfrom(1500)

                        if data[0] == 4 and int.from_bytes(data[3:5], byteorder='big', signed=False) == seq + 1:
                            size_of_data = int.from_bytes(data[1:3], byteorder='big', signed=False)
                            order_of_packet = int.from_bytes(data[3:5], byteorder='big', signed=False)
                            number_of_packets = int.from_bytes(data[5:7], byteorder='big', signed=False)

                            if crc16(data[7:]) == 0:  # crc check
                                msg += data[7:7 + size_of_data].decode(errors='replace')
                                send(server_socket, client_address, 5, 0, order_of_packet, number_of_packets, "", 0)
                                seq += 1
                            else:
                                errors += 1  # crc found mistake
                                send(server_socket, client_address, 3, 0, order_of_packet, number_of_packets, "", 0)

                        elif data[0] == 2 and int.from_bytes(data[3:5], byteorder='big', signed=False) == seq + 1:  # end of message
                            size_of_data = int.from_bytes(data[1:3], byteorder='big', signed=False)
                            order_of_packet = int.from_bytes(data[3:5], byteorder='big', signed=False)
                            number_of_packets = int.from_bytes(data[5:7], byteorder='big', signed=False)

                            if crc16(data[7:]) == 0:  # crc check
                                print(size_of_data)
                                msg += data[7:7 + size_of_data].decode(errors='replace')
                                send(server_socket, client_address, 5, 0, order_of_packet, number_of_packets, "", 0)
                                print("Prisla sprava:")
                                print(msg)
                                print("Pocet paketov u ktorych bola najdena chyba: ", errors)
                                errors = 0
                                msg = ""
                                seq = 1
                                break
                            else:  # crc found mistake
                                errors += 1
                                send(server_socket, client_address, 3, 0, order_of_packet, number_of_packets, "", 0)
            else:  # crc found mistake
                errors += 1
                send(server_socket, client_address, 3, 0, order_of_packet, number_of_packets, "", 0)

        # start of file (name of the file)
        elif data[0] == 9 and int.from_bytes(data[3:5], byteorder='big', signed=False) == seq:
            order_of_packet = int.from_bytes(data[3:5], byteorder='big', signed=False)
            number_of_packets = int.from_bytes(data[5:7], byteorder='big', signed=False)

            if crc16(data[7:]) == 0:  # crc check
                size_of_data = int.from_bytes(data[1:3], byteorder='big', signed=False)
                file_name += data[7:7+size_of_data].decode(errors='replace')
                send(server_socket, client_address, 5, 0, order_of_packet, number_of_packets, "", 0)
                seq += 1

            else:  # crc found mistake
                errors += 1
                send(server_socket, client_address, 3, 0, order_of_packet, number_of_packets, "", 0)

        # end of file name and start of file content
        elif data[0] == 10 and int.from_bytes(data[3:5], byteorder='big', signed=False) == seq:
            order_of_packet = int.from_bytes(data[3:5], byteorder='big', signed=False)
            number_of_packets = int.from_bytes(data[5:7], byteorder='big', signed=False)

            if crc16(data[7:]) == 0:  # crc check
                size_of_data = int.from_bytes(data[1:3], byteorder='big', signed=False)
                send(server_socket, client_address, 5, 0, order_of_packet, number_of_packets, "", 0)
                total_size = int.from_bytes(data[1:3], byteorder='big', signed=False)
                seq += 1
                file = open(os.path.join(path_, file_name), 'wb')
                # print((data[7:7 + size_of_data]))
                file.write(data[7:7 + size_of_data])

                while seq <= number_of_packets:
                    data, client_address = server_socket.recvfrom(1500)
                    if data[0] == 4 and int.from_bytes(data[3:5], byteorder='big', signed=False) == seq:
                        order_of_packet = int.from_bytes(data[3:5], byteorder='big', signed=False)
                        number_of_packets = int.from_bytes(data[5:7], byteorder='big', signed=False)

                        if crc16(data[7:]) == 0:  # crc check
                            total_size += int.from_bytes(data[1:3], byteorder='big', signed=False)
                            size_of_data = int.from_bytes(data[1:3], byteorder='big', signed=False)
                            send(server_socket, client_address, 5, 0, order_of_packet, number_of_packets, "", 0)
                            file.write(data[7:7 + size_of_data])
                            # print((data[7:7 + size_of_data]))
                            seq += 1
                        else:  # crc found mistake
                            errors += 1
                            send(server_socket, client_address, 3, 0, order_of_packet, number_of_packets, "", 0)

                if seq == number_of_packets + 1:  # whole file arrived
                    print("Subor " + str(file_name) + " bol ulozeny do " + str(path_))
                    print("Velkost", total_size, "B")
                    print("Pocet fragmentov", seq)
                    print("Cesta: " + os.path.abspath(file_name))
                    print("Pocet paketov u ktorych bola najdena chyba: ", errors)

                else:  # whole file did not arrived
                    print("Nastala sa chyba")
                file_name = ""
                file.close()
                seq = 1
                errors = 0

            else:  # crc found mistake
                errors += 1
                send(server_socket, client_address, 3, 0, order_of_packet, number_of_packets, "", 0)


# hlavne menu
def menu():
    while True:
        print("1) KLIENT")
        print("2) SERVER")
        print("3) Koniec")
        x = input("Vyberte moznost: ")
        if x == '1':
            client()
        elif x == '2':
            server()
        elif x == '3':
            exit(0)
        else:
            print("Moznost neexistuje")


if __name__ == '__main__':
    menu()
