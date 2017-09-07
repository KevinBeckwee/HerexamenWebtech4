from django.conf.urls import url
from . import views

app_name = 'testapp'

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^(?P<product_id>[A-Z][A-Z][A-Z]\-[0-9][0-9])/$', views.detail, name='detail'),
    url(r'^(?P<product_id>[A-Z][A-Z][A-Z]\-[0-9][0-9])/delete$', views.delete, name='delete')
]
