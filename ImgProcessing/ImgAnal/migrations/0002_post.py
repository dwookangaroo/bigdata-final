# Generated by Django 3.2 on 2021-09-15 11:11

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('ImgAnal', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Post',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=200)),
                ('text', models.TextField()),
            ],
        ),
    ]
