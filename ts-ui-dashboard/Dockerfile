FROM openresty/openresty:trusty

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
RUN mkdir -p /var/log/nginx && \
    touch /var/log/nginx/access.log 
    
COPY nginx.conf /usr/local/openresty/nginx/conf/nginx.conf

COPY static /usr/share/nginx/html