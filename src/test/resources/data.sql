INSERT INTO public.users(
	name, email)
	VALUES
('updateName','updateName@user.com'),
('user','user@user.com'),
('other','other@other.com'),
('user4','user4@user.com'),
('user5','user5@user.com'),
('user6','user6@user.com');

INSERT INTO public.requests(
	description, requester_id, created)
	VALUES
('Хотел бы воспользоваться щёткой для обуви',1,'2024-05-31 00:16:48.02615');

INSERT INTO public.items(
	name, description, is_available, owner_id, request_id)
	VALUES
('Аккумуляторная дрель','Аккумуляторная дрель + аккумулятор','t',1,NULL),
('Отвертка','Аккумуляторная отвертка','t',4,NULL),
('Клей Момент','Тюбик суперклея марки Момент','t',4,NULL),
('Кухонный стол','Стол для празднования','t',6,NULL),
('Щётка для обуви','Стандартная щётка для обуви','t',4,1);

INSERT INTO public.bookings(
	start_time, end_time, item_id, booker_id, status)
	VALUES 
('2024-05-31 00:16:35','2024-05-31 00:16:36',2,1,'APPROVED'),
('2024-06-01 00:16:32','2024-06-02 00:16:32',2,1,'APPROVED'),
('2024-06-01 00:16:34','2024-06-01 01:16:34',1,4,'REJECTED'),
('2024-05-31 01:16:34','2024-05-31 02:16:34',2,5,'APPROVED'),
('2024-05-31 00:16:41','2024-06-01 00:16:38',3,1,'REJECTED'),
('2024-05-31 00:16:42','2024-05-31 00:16:43',2,1,'APPROVED'),
('2024-05-31 00:16:41','2024-05-31 01:16:39',4,1,'APPROVED'),
('2024-06-10 00:16:39','2024-06-11 00:16:39',1,5,'APPROVED');

INSERT INTO public.comments(
	text, item_id, author_id, created)
	VALUES
('Add comment from user1',2,1,'2024-05-31 00:16:46.408827');




