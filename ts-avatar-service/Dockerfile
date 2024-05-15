FROM python:3

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

RUN apt update -y
RUN apt install -y cmake
RUN apt install -y libgl1-mesa-glx

ENV PYTHONUNBUFFERED=TRUE

RUN mkdir -p /app
ADD . /app/
WORKDIR /app

RUN pip install --upgrade pip
RUN pip3 install numpy
RUN pip3 install opencv-python
RUN pip3 install Pillow
RUN pip3 install dlib==19.21.1
RUN pip3 install MarkupSafe==1.1.1
RUN pip install -r requirements.txt

CMD ["gunicorn", "-w", "4", "-b", "0.0.0.0:17001", "app:app"]

EXPOSE 17001