# Generated by Django 3.2 on 2021-10-16 17:31

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('ImgAnal', '0006_auto_20211015_1712'),
    ]

    operations = [
        migrations.AddField(
            model_name='restaurants',
            name='english_operation_time',
            field=models.CharField(default='', max_length=255),
        ),
        migrations.AddField(
            model_name='restaurants',
            name='english_service_option',
            field=models.CharField(default='', max_length=255),
        ),
    ]
