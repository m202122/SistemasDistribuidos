import sys

from tutorial import Agenda
from tutorial.ttypes import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

try:

   # Make socket
   transport = TSocket.TSocket('localhost', 9090)

   # Buffering is critical. Raw sockets are very slow
   transport = TTransport.TBufferedTransport(transport)

   # Wrap in a protocol
   protocol = TBinaryProtocol.TBinaryProtocol(transport)

   # Create a client to use the protocol encoder
   client = Agenda.Client(protocol)
   
   # Connect!
   transport.open()

   print 'Sending User Info to Server'
   ans = True
   while ans:
      print("""
         ----------- AGENDA -----------
         0. Imprimir todos os registros
         1. Armazenar/Atualizar
         2. Remover
         3. Recuperar
         4. Sair
         """)
      ans = raw_input("O que deseja fazer? ")
      if ans=="0":
         print client.getDatabase()
      elif ans=="1":
         nome = raw_input("Insira o nome: ")
         telefone = raw_input("Insira o telefone: ")
         if client.search(nome) == "Registro nao encontrado!":
            print client.insert(nome,telefone,2)
         else:
            substituir = raw_input("Ja existe um registro com esse nome, substituir? (S/N)")
            if substituir.lower() == "s":
               print client.insert(nome,telefone,1)
            else:
               print client.insert(nome,telefone,0)
      elif ans=="2":
         nome = raw_input("Insira o nome do registro a ser deletado: ")
         print client.remove(nome)
      elif ans=="3":
         nome = raw_input("Insira o nome do registro a ser recuperado: ")
         print client.search(nome)
      elif ans=="4":
         print("\n Saindo...")
         ans = False
      elif ans !="":
         print("\n Opcao invalida!")
   
   # Close!
   transport.close()

except Thrift.TException, tx:
    print '%s' % (tx.message)
