from mixer.backend.django import mixer
import pytetst


@pytest.mark.django_db
class TestModels:
    def test_user_exists(stock):
        user = mixer.blend('kotoUser.user',id=1)
        assert user.exists == True
