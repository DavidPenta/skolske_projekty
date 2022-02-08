import scapy.all as scapy

file_read = "C:\\Users\\david\\Desktop\\vzorky_pcap_na_analyzu\\eth-1.pcap"
file_write = "PKS1-output.txt"  # vystup pre ulohy 1-3

f_write = open(file_write, "w")


count = 0  # cislo ramca
SourceIPAddresses = {}  # zoznam vsetkych odosielajucich IPv4 uzlov a pocet odoslanych paketov

# zoznam vsetkych ramcov usoriadanych do komunikacii
HTTP_communications_list = []
HTTPS_communications_list = []
TELNET_communications_list = []
SSH_communications_list = []
FTP_control_communications_list = []
FTP_data_communications_list = []
TFTP_communications_list = []
ICMP_communications_list = []
ARP_communications_list = []


class TCP_communication:
    def __init__(self, ip1, ip2, port, tcp):
        self. completed = 0
        self.completed1 = 0
        self.completed2 = 0
        self.ip1 = ip1
        self.ip2 = ip2
        self.frames = []
        self.port = port
        if tcp.flags == 2:  # SYN
            self.frames.append(tcp)

    def add_to_TCP_communication(self, tcp):
        if len(self.frames) == 1 and tcp.flags == 18 and tcp.dIP == self.frames[0].sIP:  # SYN ACK
            self.frames.append(tcp)

        elif len(self.frames) == 2 and tcp.flags == 16 and tcp.dIP == self.frames[0].dIP:  # ACK
            self.frames.append(tcp)

        elif len(self.frames) > 2:  # komunikacia uz bola uspesne zacata
            self.frames.append(tcp)

            if tcp.flags % 8 >= 4:  # < RES, komunikacia je kompletna
                self.completed = 1

            if tcp.flags % 2 == 1:  # strana poslala FIN
                if tcp.dIP == self.ip1:
                    self.completed1 = 1
                if tcp.dIP == self.ip2:
                    self.completed2 = 1

            if self.completed1 == 1 and self.completed2 == 1:  # obe strany ukoncily s FIN
                self.completed = 1

    def compare_to_TCP_communication(self, ip1, ip2, port, tcp):
        if self.port == port and ((self.ip1 == ip1 and self.ip2 == ip2) or (self.ip1 == ip2 and self.ip2 == ip1)):  # zistenie ci ramec patri do tejto komunikacie
            self.add_to_TCP_communication(tcp)
            return True
        else:
            return False

    def print_TCP_communication(self):
        if len(self.frames) <= 20:
            for i in self.frames:
                i.print_TCP()
        else:
            for i in self.frames[:10]:
                i.print_TCP()
            for i in self.frames[-10:]:
                i.print_TCP()


class TCP:
    def __init__(self, n, length_API, data_link, TCP_port, s_mac, d_mac, s_ip, d_ip, s_port, d_port, flags, content):
        self.count = str(n)
        self.length = length_API
        self.length2 = length_API + 4
        if self.length2 < 64:
            self.length2 = 64
        self.dataLink = data_link
        self.port = TCP_port
        self.sMAC = s_mac
        self.dMAC = d_mac
        self.sIP = s_ip
        self.dIP = d_ip
        self.sPort = s_port
        self.dPort = d_port
        self.flags = flags
        self.text = content

    def print_TCP(self):
        print("Ramec: " + self.count)
        print("Dlzka ramca poskytnuta pcap API: " + str(self.length) + ' B')
        print("Dlzka ramca poskytnuta po mediu: " + str(self.length2) + ' B')
        print("Zdrojova MAC adresa: " + self.sMAC)
        print("Cielova MAC adresa: " + self.dMAC)
        print(self.dataLink)
        print("IPv4")
        print("Zdrojova IP adresa: " + self.sIP)
        print("Cielova IP adresa: " + self.dIP)
        print("TCP")
        print(self.port)
        print("Zdrojovy port: " + self.sPort)
        print("Cielovy port: " + self.dPort)
        f = self.flags
        if f - 32 >= 0:  # ake flagy obsahuje
            print("URG")
            f -= 32
        if f - 16 >= 0:
            print("ACK")
            f -= 16
        if f - 8 >= 0:
            print("PUSH")
            f -= 8
        if f - 4 >= 0:
            print("RES")
            f -= 4
        if f - 2 >= 0:
            print("SYN")
            f -= 2
        if f - 1 >= 0:
            print("FIN")
            f -= 1
        print(self.text)
        print('\n')


class TFTP_communication:
    def __init__(self, s_mac, d_mac, s_ip, d_ip, s_port, d_port, tftp):
        self.sMAC = s_mac
        self.dMAC = d_mac
        self.sIP = s_ip
        self.dIP = d_ip
        self.sPort = s_port
        self.dPort = d_port
        self.frames = []
        self.frames.append(tftp)

    def compare_to_TFTP_communication(self, s_mac, d_mac, s_ip, d_ip, s_port, d_port, tftp):  # zistenie ci ramec patri do tejto komunikacie
        if (s_mac == self.sMAC and d_mac == self.dMAC and s_ip == self.sIP and d_ip == self.dIP and ((str(s_port) == self.dPort or str(s_port) == self.sPort) or (str(d_port) == self.dPort or str(d_port) == self.sPort))) or (d_mac == self.sMAC and s_mac == self.dMAC and d_ip == self.sIP and s_ip == self.dIP and ((str(s_port) == self.dPort or str(s_port) == self.sPort) or (str(d_port) == self.dPort or str(d_port) == self.sPort))):
            if self.sPort == "69":
                if str(d_port) == self.dPort:
                    self.sPort = str(s_port)
                if str(s_port) == self.dPort:
                    self.sPort = str(d_port)
            elif self.dPort == "69":
                if str(d_port) == self.sPort:
                    self.dPort = str(s_port)
                if str(s_port) == self.sPort:
                    self.dPort = str(d_port)
            else:
                if (str(s_port) != self.sPort or str(d_port) != self.dPort) and (str(d_port) != self.sPort or str(s_port) != self.dPort):
                    return False
            self.frames.append(tftp)
            return True
        else:
            return False

    def print_TFTP_communication(self):
        if len(self.frames) <= 20:
            for i in self.frames:
                i.print_TFTP()
        else:
            for i in self.frames[:10]:
                i.print_TFTP()
            for i in self.frames[-10:]:
                i.print_TFTP()


class TFTP:
    def __init__(self, n, length_API, data_link, s_mac, d_mac, s_ip, d_ip, s_port, d_port, content):
        self.count = str(n)
        self.length = length_API
        self.length2 = length_API + 4
        if self.length2 < 64:
            self.length2 = 64
        self.dataLink = data_link
        self.sMAC = s_mac
        self.dMAC = d_mac
        self.sIP = s_ip
        self.dIP = d_ip
        self.sPort = s_port
        self.dPort = d_port
        self.text = content

    def print_TFTP(self):
        print("Ramec: " + self.count)
        print("Dlzka ramca poskytnuta pcap API: " + str(self.length) + ' B')
        print("Dlzka ramca poskytnuta po mediu: " + str(self.length2) + ' B')
        print("Zdrojova MAC adresa: " + self.sMAC)
        print("Cielova MAC adresa: " + self.dMAC)
        print(self.dataLink)
        print("IPv4")
        print("Zdrojova IP adresa: " + self.sIP)
        print("Cielova IP adresa: " + self.dIP)
        print("UDP")
        print("TFTP")
        print("Zdrojovy port: " + self.sPort)
        print("Cielovy port: " + self.dPort)
        print(self.text)
        print('\n')


class ICMP_communication:
    def __init__(self, ip1, ip2, icmp):
        self.ip1 = ip1
        self.ip2 = ip2
        self.frames = []
        self.frames_echo = []
        self.frames_echo_reply = []
        if icmp.type == "Echo":
            self.frames_echo.append(icmp)
        elif icmp.type == "Echo Reply":
            self.frames_echo_reply.append(icmp)
        else:
            self.frames.append(icmp)

    def compare_to_ICMP_communication(self, ip_1, ip_2, icmp):  # zistenie ci patri do tejto komunikacie
        if (self.ip1 == str(ip_1) and self.ip2 == str(ip_2)) or (self.ip1 == str(ip_2) and self.ip2 == str(ip_1)):
            if icmp.type == "Echo":
                self.frames_echo.append(icmp)
            elif icmp.type == "Echo Reply":
                self.frames_echo_reply.append(icmp)
            else:
                self.frames.append(icmp)
            return True
        else:
            return False

    def print_ICMP_communication(self):
        for i in range(min(len(self.frames_echo), len(self.frames_echo_reply))):
            print("IMCP komunikacia Echo - Echo Reply medzi " + self.ip1 + " a " + self.ip2)
            self.frames_echo[i].print_ICMP()
            self.frames_echo_reply[i].print_ICMP()

        if min(len(self.frames_echo), len(self.frames_echo_reply)) < 1:
            for j in self.frames_echo:
                j.print_ICMP()
            for j in self.frames_echo_reply:
                j.print_ICMP()

        if len(self.frames) > 0:
            print("IMCP komunikacie ine ako Echo a Echo Reply medzi " + self.ip1 + " a " + self.ip2)
            for j in self.frames:
                j.print_ICMP()


class ICMP:
    def __init__(self, n, length_API, data_link, ICMP_type, s_mac, d_mac, s_ip, d_ip, content):
        self.count = str(n)
        self.length = length_API
        self.length2 = length_API + 4
        if self.length2 < 64:
            self.length2 = 64
        self.dataLink = data_link
        self.type = ICMP_type
        self.sMAC = s_mac
        self.dMAC = d_mac
        self.sIP = s_ip
        self.dIP = d_ip
        self.text = content

    def print_ICMP(self):
        print("Ramec: " + self.count)
        print("Dlzka ramca poskytnuta pcap API: " + str(self.length) + ' B')
        print("Dlzka ramca poskytnuta po mediu: " + str(self.length2) + ' B')
        print("Zdrojova MAC adresa: " + self.sMAC)
        print("Cielova MAC adresa: " + self.dMAC)
        print(self.dataLink)
        print("IPv4")
        print("Zdrojova IP adresa: " + self.sIP)
        print("Cielova IP adresa: " + self.dIP)
        print("ICMP")
        print(self.type)
        print(self.text)
        print('\n')


class ARP_communication:
    def __init__(self, operation, mac1, mac2, ip1, ip2, arp):
        self.address1 = ip1
        self.address2 = ip2
        self.MAC1 = mac1
        self.MAC2 = mac2
        self.requests = []
        self.replies = []
        self. completed = 0

        if operation == 1:
            self.requests.append(arp)
        if operation == 2:
            self.replies.append(arp)

    def change_completed(self):
        if len(self.requests) > 0 and len(self.replies) > 0:
            self.completed = 1
            self.MAC2 = self.replies[0].sMAC

    def compare_to_ARP_communication(self, s_add, d_add, operation, arp):  # zistenie ci patri do tejto komunikacie
        if operation == 1 and self.address1 == s_add and self.address2 == d_add:
            self.requests.append(arp)
            self.change_completed()
            return True
        elif operation == 2 and self.address1 == d_add and self.address2 == s_add:
            self.replies.append(arp)
            self.change_completed()
            return True
        else:
            return False

    def print_ARP_communication(self, v):
        print("Komunikacia cislo: " + str(v))
        print("Pocet request: " + str(len(self.requests)) + "\nPocet reply: " + str(len(self.replies)))
        print()
        print("ARP-Request, IP adresa: " + self.address2 + ", MAC adresa: ???")
        print("Zdrojova IP: " + self.address1 + ", Cielova IP:" + self.address2 + "\n")
        for i in self.requests:
            i.print_ARP()
        if len(self.replies) > 0:
            print("ARP-Reply, IP adresa: " + self.address2 + ", MAC adresa: " + self.MAC2)
            print("Zdrojova IP: " + self.address2 + ", Cielova IP:" + self.address1 + "\n")
            for i in self.replies:
                i.print_ARP()


class ARP:
    def __init__(self, n, length_API, data_link, s_mac, d_mac, s_address, d_address, operation, content):
        self.count = str(n)
        self.length = length_API
        self.length2 = length_API + 4
        if self.length2 < 64:
            self.length2 = 64
        self.dataLink = data_link
        self.sMAC = s_mac
        self.dMAC = d_mac
        self.sAdd = str(s_address)
        self.dAdd = str(d_address)
        if operation == 1:  # request
            self.Add = d_address
            self.MAC = d_mac
        else:  # reply
            self.Add = s_address
            self.MAC = s_mac
        self.op = operation
        self.text = content

    def print_ARP(self):
        print("Ramec: " + self.count)
        print("Dlzka ramca poskytnuta pcap API: " + str(self.length) + ' B')
        print("Dlzka ramca poskytnuta po mediu: " + str(self.length2) + ' B')
        print("Zdrojova MAC adresa: " + self.sMAC)
        print("Cielova MAC adresa: " + self.dMAC)
        print(self.dataLink)
        print("ARP")
        if self.op == 1:
            print("Request")
        if self.op == 2:
            print("Reply")
        print("Zdrojova adresa: " + self.sAdd)
        print("Cielova adresa: " + self.dAdd)
        print(self.text)
        print('\n')


def get_EtherType(value):  # najdenie mena  v subore podla hodnoty
    with open('EtherTypes.txt') as f:
        for ln in f:
            if ln.startswith(str(value)):
                return ln[len(value) + 1:].strip()
    return "EtherType: " + value + " -Unknown"


def get_Sap(value):  # najdenie mena  v subore podla hodnoty
    with open('SAPs.txt') as f:
        for ln in f:
            if ln.startswith(str(value)):
                return ln[len(value) + 1:].strip()

    return "Type: " + value + " -Unknown"


def get_IPv4_protocol(value):  # najdenie mena  v subore podla hodnoty
    with open('IPv4_Protocols.txt') as f:
        for ln in f:
            if ln.startswith(str(value)):
                return ln[len(value) + 1:].strip()

    return "Protocl: " + value + " -Unknown"


def get_TCP_port(value):  # najdenie mena  v subore podla hodnoty
    with open('TCP_Ports.txt') as f:
        for ln in f:
            if ln.startswith(str(value)):
                return ln[len(value) + 1:].strip()
    return "Port: " + value + " -Unknown"


def get_UDP_port(value):  # najdenie mena  v subore podla hodnoty
    with open('UDP_Ports.txt') as f:
        for ln in f:
            if ln.startswith(str(value)):
                return ln[len(value) + 1:].strip()
    return " "


def get_ICMP_type(value):  # najdenie mena  v subore podla hodnoty
    with open('ICMP_Types.txt') as f:
        for ln in f:
            if ln.startswith(str(value)):
                return ln[len(value) + 1:].strip()
    return "Type: " + value + " -Unknown"


def get_source_IPv4_IP_address(value):
    source_ip = str(int(value[0])) + '.' + str(int(value[1])) + '.' + str(int(value[2])) + '.' + str(int(value[3]))
    f_write.write("Zdrojova IP adresa: " + source_ip + '\n')
    if source_ip in SourceIPAddresses:
        SourceIPAddresses[source_ip] += 1  # zvisenie poctu ip adresy
    else:
        SourceIPAddresses[source_ip] = 1  # pridanie novej zdrojovej ip adresy
    return source_ip


def get_destination_IPv4_IP_address(value):
    destination_ip = str(int(value[0])) + '.' + str(int(value[1])) + '.' + str(int(value[2])) + '.' + str(int(value[3]))
    f_write.write("Cielova IP adresa: " + destination_ip + '\n')
    return destination_ip


def get_destination_ARP_IP_address(value):
    destination_ip = str(int(value[0])) + '.' + str(int(value[1])) + '.' + str(int(value[2])) + '.' + str(int(value[3]))
    return destination_ip


def get_source_ARP_IP_address(value):
    source_ip = str(int(value[0])) + '.' + str(int(value[1])) + '.' + str(int(value[2])) + '.' + str(int(value[3]))
    return source_ip


def uloha1(frame):
    f_write.write("Ramec " + str(count) + '\n')
    # zistenie dlzok
    length = len(frame)
    f_write.write("Dlzka ramca poskytnuta pcap API: " + str(length) + ' B \n')
    length2 = len(frame) + 4
    if length2 < 64:
        length2 = 64
    f_write.write("Dlzka ramca prenasaneho po mediu: " + str(length2) + ' B \n')
    # zistenie MAC adries
    sMAC = " ".join([str(frame[6:12].hex())[i:i + 2] for i in range(0, 12, 2)])
    dMAC = " ".join([str(frame[0:6].hex())[i:i + 2] for i in range(0, 12, 2)])
    f_write.write("Zdrojova MAC adresa: " + sMAC + '\n')
    f_write.write("Cielova MAC adresa: " + dMAC + '\n')

    dataLink = ""
    etherType = ""
    if int.from_bytes(frame[12:14], byteorder='big') > 1500:  # Ethernet II
        f_write.write("Ethernet II\n")
        dataLink = "Ethernet II"
        etherType = get_EtherType(str(int.from_bytes(frame[12:14], byteorder='big')))  # zistenie EtherTypu
        f_write.write(etherType + '\n')

    else:  # IEEE 802.2
        if frame[14] == 255:  # FF
            f_write.write("IEEE 802.3 - \"Raw\" \nIPX\n")
        elif frame[14] == 170:  # AA
            dataLink = "IEEE 802.3 - LLC + SNAP"
            f_write.write("IEEE 802.3 - LLC + SNAP \n")
            etherType = get_EtherType(str(int.from_bytes(frame[20:22], byteorder='big')))  # zistenie EtherTypu
            f_write.write(etherType + '\n')
        else:
            f_write.write("IEEE 802.3 - LLC \n")
            f_write.write(get_Sap(str(int.from_bytes(frame[14:15], byteorder='big'))) + '\n')  # zistenie SAPu

    text = ""  # usporiadanie vypisu
    for i in range(0, len(frame.hex()), 2):
        text += str(frame.hex())[i:i + 2]
        if (i + 2) % 32 == 0:
            text += '\n'
        elif (i + 2) % 16 == 0:
            text += '  '
        else:
            text += ' '

    if etherType == "IPv4":
        sIP = get_source_IPv4_IP_address(frame[26:30])
        dIP = get_destination_IPv4_IP_address(frame[30:34])
        IPv4_protocol = get_IPv4_protocol(str(int(frame[23])))
        f_write.write(IPv4_protocol + '\n')
        if IPv4_protocol == "TCP" or IPv4_protocol == "UDP" or IPv4_protocol == "ICMP":
            n = ((int(frame[14]) % 16) * 4) + 14  # zistenie dlzky options v IPv4 hlavicke a kde sa zacina dalsi protokol
            sPort = int.from_bytes(frame[n: n + 2], byteorder='big')  # pre TCP a UDP
            dPort = int.from_bytes(frame[n + 2: n + 4], byteorder='big')
            if IPv4_protocol == "TCP":
                IPv4_port = get_TCP_port(str(min(sPort, dPort)))
                f_write.write(IPv4_port + '\n')
                flags = (int.from_bytes(frame[n + 12: n + 14], byteorder='big')) % 64
                p = 0

                if IPv4_port == "http":
                    a = TCP(count, length, dataLink, IPv4_port, sMAC, dMAC, sIP, dIP, str(sPort), str(dPort), flags, text)
                    # HTTP_list.append(a)  # zapamatanie vsetkych HTTP ramcov
                    for c in HTTP_communications_list:  # pridanie HTTP ramca do spravnej komunikacie
                        if c.compare_to_TCP_communication(sIP, dIP, max(sPort, dPort), a):
                            p = 1
                            break
                    if p == 0:
                        if a.flags == 2:    # vytvorenie novej komunikacie
                            b = TCP_communication(sIP, dIP, max(sPort, dPort), a)
                            HTTP_communications_list.append(b)

                elif IPv4_port == "https (ss1)":
                    a = TCP(count, length, dataLink, IPv4_port, sMAC, dMAC, sIP, dIP, str(sPort), str(dPort), flags, text)
                    # HTTPS_list.append(a)
                    for c in HTTPS_communications_list:
                        if c.compare_to_TCP_communication(sIP, dIP, max(sPort, dPort), a):
                            p = 1
                            break
                    if p == 0:
                        if a.flags == 2:
                            b = TCP_communication(sIP, dIP, max(sPort, dPort), a)
                            HTTPS_communications_list.append(b)

                elif IPv4_port == "telnet":
                    a = TCP(count, length, dataLink, IPv4_port, sMAC, dMAC, sIP, dIP, str(sPort), str(dPort), flags, text)
                    # TELNET_list.append(a)
                    for c in TELNET_communications_list:
                        if c.compare_to_TCP_communication(sIP, dIP, max(sPort, dPort), a):
                            p = 1
                            break
                    if p == 0:
                        if a.flags == 2:
                            b = TCP_communication(sIP, dIP, max(sPort, dPort), a)
                            TELNET_communications_list.append(b)

                elif IPv4_port == "ssh":
                    a = TCP(count, length, dataLink, IPv4_port, sMAC, dMAC, sIP, dIP, str(sPort), str(dPort), flags, text)
                    # SSH_list.append(a)
                    for c in SSH_communications_list:
                        if c.compare_to_TCP_communication(sIP, dIP, max(sPort, dPort), a):
                            p = 1
                            break
                    if p == 0:
                        if a.flags == 2:
                            b = TCP_communication(sIP, dIP, max(sPort, dPort), a)
                            SSH_communications_list.append(b)

                elif IPv4_port == "ftp-control":
                    a = TCP(count, length, dataLink, IPv4_port, sMAC, dMAC, sIP, dIP, str(sPort), str(dPort), flags, text)
                    # FTP_control_list.append(a)
                    for c in FTP_control_communications_list:
                        if c.compare_to_TCP_communication(sIP, dIP, max(sPort, dPort), a):
                            p = 1
                            break
                    if p == 0:
                        if a.flags == 2:
                            b = TCP_communication(sIP, dIP, max(sPort, dPort), a)
                            FTP_control_communications_list.append(b)

                elif IPv4_port == "ftp-data":
                    a = TCP(count, length, dataLink, IPv4_port, sMAC, dMAC, sIP, dIP, str(sPort), str(dPort), flags, text)
                    # FTP_data_list.append(a)
                    for c in FTP_data_communications_list:
                        if c.compare_to_TCP_communication(sIP, dIP, max(sPort, dPort), a):
                            p = 1
                            break
                    if p == 0:
                        if a.flags == 2:
                            b = TCP_communication(sIP, dIP, max(sPort, dPort), a)
                            FTP_data_communications_list.append(b)

            if IPv4_protocol == "ICMP":
                ICMP_type = get_ICMP_type(str(int(frame[n])))
                f_write.write(ICMP_type + '\n')
                a = ICMP(count, length, dataLink, ICMP_type, sMAC, dMAC, sIP, dIP, text)
                # ICMP_list.append(a)
                p = 0
                for c in ICMP_communications_list:
                    if c.compare_to_ICMP_communication(sIP, dIP, a):
                        p = 1
                        break
                if p == 0:
                    b = ICMP_communication(sIP, dIP, a)
                    ICMP_communications_list.append(b)

            if IPv4_protocol == "UDP":
                UDP_port = get_UDP_port(str(min(sPort, dPort)))
                if UDP_port != " ":
                    f_write.write(UDP_port + '\n')
                a = TFTP(count, length, dataLink, sMAC, dMAC, sIP, dIP, str(sPort), str(dPort), text)
                # TFTP_list.append(a)
                if min(sPort, dPort) == 69:  # TFTP
                    b = TFTP_communication(sMAC, dMAC, sIP, dIP,  str(sPort), str(dPort), a)
                    TFTP_communications_list.append(b)
                else:
                    for c in TFTP_communications_list:
                        if c.compare_to_TFTP_communication(sMAC, dMAC, sIP, dIP, sPort, dPort, a):
                            f_write.write("tftp" + '\n')
                            break

            f_write.write("Zdrojovy port: " + str(sPort) + '\n')
            f_write.write("Cielovy port: " + str(dPort) + '\n')

    elif etherType == "ARP":
        operation = int.from_bytes(frame[20:22], byteorder='big')
        d_address = get_destination_ARP_IP_address(frame[38:42])
        s_address = get_source_ARP_IP_address(frame[28:32])
        a = ARP(count, length, dataLink, sMAC, dMAC, s_address, d_address, operation, text)
        # ARP_list.append(a)
        p = 0
        for c in ARP_communications_list:
            if c.compare_to_ARP_communication(s_address, d_address, operation, a):
                p = 1
                break
        if p == 0:
            b = ARP_communication(operation, sMAC, dMAC, s_address, d_address, a)
            ARP_communications_list.append(b)

    f_write.write(text)
    f_write.write('\n\n')


def printIPAddresses():
    if len(SourceIPAddresses) > 0:
        f_write.write("IP adresy vysielajucich uzlov:\n")
        for i in SourceIPAddresses:
            f_write.write(i + "\n")
        mostUsedIPAddress = max(SourceIPAddresses, key=SourceIPAddresses.get)
        f_write.write("Adresa uzla s najvacsim poctom odoslanych paketov:\n" + str(mostUsedIPAddress) + " - " + str(
            SourceIPAddresses[mostUsedIPAddress]) + " paketov")
    else:
        f_write.write("Subor neobsahuje IPv4\n")


def program():
    while True:
        print("\nVypis HTTP komunikacie: 1")
        print("Vypis HTTPS komunikacie: 2")
        print("Vypis TELNET komunikacie: 3")
        print("Vypis SSH komunikacie: 4")
        print("Vypis FTP-riadiace komunikacie: 5")
        print("Vypis FTP-datove komunikacie: 6")
        print("Vypis TFTP komunikacie: 7")
        print("Vypis ICMP komunikacie: 8")
        print("Vypis ARP komunikacie: 9")
        print("Koniec: 0")
        x = input("Zadajte akciu: ")
        if x == "1":
            print("Pocet HTTP komunikacii s 3- way handshake: " + str(len(HTTP_communications_list)))
            print("Kompletna komunikacia:")
            for i in HTTP_communications_list:
                if i.completed == 1:
                    i.print_TCP_communication()
                    break

            print("Nekompletna komunikacia:")
            for i in HTTP_communications_list:
                if i.completed == 0:
                    i.print_TCP_communication()
                    break

        elif x == "2":
            print("Pocet HTTPS komunikacii s 3- way handshake: " + str(len(HTTPS_communications_list)))
            print("Kompletna komunikacia:")
            for i in HTTPS_communications_list:
                if i.completed == 1:
                    i.print_TCP_communication()
                    break

            print("Nekompletna komunikacia:")
            for i in HTTPS_communications_list:
                if i.completed == 0:
                    i.print_TCP_communication()
                    break
        elif x == "3":
            print("Pocet TELNET komunikacii s 3- way handshake: " + str(len(TELNET_communications_list)))
            print("Kompletna komunikacia:")
            for i in TELNET_communications_list:
                if i.completed == 1:
                    i.print_TCP_communication()
                    break

            print("Nekompletna komunikacia:")
            for i in TELNET_communications_list:
                if i.completed == 0:
                    i.print_TCP_communication()
                    break
        elif x == "4":
            print("Pocet SSH komunikacii s 3- way handshake: " + str(len(SSH_communications_list)))
            print("Kompletna komunikacia:")
            for i in SSH_communications_list:
                if i.completed == 1:
                    i.print_TCP_communication()
                    break

            print("Nekompletna komunikacia:")
            for i in SSH_communications_list:
                if i.completed == 0:
                    i.print_TCP_communication()
                    break
        elif x == "5":
            print("Pocet FTP-control komunikacii s 3- way handshake: " + str(len(FTP_control_communications_list)))
            print("Kompletna komunikacia:")
            for i in FTP_control_communications_list:
                if i.completed == 1:
                    i.print_TCP_communication()
                    break

            print("Nekompletna komunikacia:")
            for i in FTP_control_communications_list:
                if i.completed == 0:
                    i.print_TCP_communication()
                    break
        elif x == "6":
            print("Pocet FTP-data komunikacii s 3- way handshake: " + str(len(FTP_data_communications_list)))
            print("Kompletna komunikacia:")
            for i in FTP_data_communications_list:
                if i.completed == 1:
                    i.print_TCP_communication()
                    break

            print("Nekompletna komunikacia:")
            for i in FTP_data_communications_list:
                if i.completed == 0:
                    i.print_TCP_communication()
                    break

        elif x == "7":
            for i in range(len(TFTP_communications_list)):
                print("TFTP komunikacia cislo: " + str(i + 1))
                TFTP_communications_list[i].print_TFTP_communication()

        elif x == "8":
            for i in ICMP_communications_list:
                i.print_ICMP_communication()

        elif x == "9":
            v = 1
            for i in ARP_communications_list:
                if i.completed == 1:
                    i.print_ARP_communication(v)
                    v += 1
            for i in ARP_communications_list:
                if i.completed == 0:
                    i.print_ARP_communication(v)
                    v += 1
        else:
            break

print("Nacitany subor s protokolmi: " + file_read)
print("Vystupny subor:  " + file_write)
pcap = scapy.rdpcap(file_read)

for packet in pcap:
    raw = scapy.raw(packet)
    count += 1
    uloha1(raw)

printIPAddresses()
f_write.flush()
f_write.close()
program()
