# Generated by Django 2.1 on 2018-09-02 20:02

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0012_transaction_amount'),
    ]

    operations = [
        migrations.AddField(
            model_name='transaction',
            name='Details',
            field=models.CharField(default=2, max_length=1000),
            preserve_default=False,
        ),
    ]
