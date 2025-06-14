from locust import HttpUser, TaskSet, task, between

class SearchTasks(TaskSet):
  @task
  def basic_search(self):
    self.client.get("/", name="Homepage")
    keywords = ["telefon", "bilgisayar", "spor ayakkabı"]
    for kw in keywords:
      with self.client.get(f"/arama", params={"q": kw}, name="Search", catch_response=True) as resp:
        if resp.status_code != 200 or "class=\"prodList" not in resp.text:
          resp.failure(f"Search failed for '{kw}'")
    with self.client.get("/arama", params={"q": ""}, name="Empty Search", catch_response=True) as resp:
      if resp.status_code != 200:
        resp.failure("Empty search did not return 200")
      elif "Sonuç bulunamadı" not in resp.text:
        resp.success()

class WebsiteUser(HttpUser):
  tasks = [SearchTasks]
  wait_time = between(1, 3)
  host = "https://www.n11.com"
