import requests
import json

f = open('Tester/config.json', 'rt', encoding='UTF-8')

config = json.load(f)

host = config['host'] + ":" + config['port']
path = config['path']

params = config['params']

headers = {'User-Agent' : 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0'}
print(headers)

host += path
response = requests.get(host, params=params, headers=headers)

print(response.url)

print(response.text)