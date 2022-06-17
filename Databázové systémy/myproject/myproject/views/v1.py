from rest_framework.response import Response
from rest_framework.decorators import api_view
import psycopg2
import json
import os


@api_view(['GET'])
def getData_V1(request):
    connection = psycopg2.connect(dbname="dota2", user=os.environ.get('USER_NAME'), password=os.environ.get('USER_PASSWORD'), host="147.175.150.216", port="5432")
    crsr = connection.cursor()
    crsr.execute('SELECT version()')
    dictionary = {'version': crsr.fetchone()[0]}

    crsr.execute("SELECT pg_database_size('dota2')/1024/1024 as dota2_db_size")
    dictionary['dota2_db_size'] = crsr.fetchone()[0]
    j = {"pgsql": dictionary}

    crsr.close()
    connection.close()
    return Response(json.loads(json.dumps(j)))
