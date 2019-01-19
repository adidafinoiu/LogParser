-- (1) Write MySQL query to find IPs that mode more than a certain number of requests for a given time period.

SELECT IP
FROM LOG
WHERE DATE_TIME BETWEEN '2017-01-01.13:00:00' AND '2017-01-01.14:00:00'
GROUP BY IP
HAVING count(IP) > 100;

-- (2) Write MySQL query to find requests made by a given IP.

SELECT ID, DATE_TIME, IP, REQUEST, STATUS, USER_AGENT
FROM LOG
WHERE IP = '192.168.106.134';
