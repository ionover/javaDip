FROM nginx:1.25-alpine
LABEL MAINTAINER="ionov <>"

COPY dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]