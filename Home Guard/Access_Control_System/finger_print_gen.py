import cv2
import os
import csv

def compare_fingerprints(image1_path, image2_path):
    # Read the fingerprint images and convert them to GRAYSCALE
    img1 = cv2.imread(image1_path, cv2.IMREAD_GRAYSCALE)
    img2 = cv2.imread(image2_path, cv2.IMREAD_GRAYSCALE)

    # Initialize SIFT detector
    sift = cv2.SIFT_create()

    # Find keypoints and descriptors
    kp1, des1 = sift.detectAndCompute(img1, None)
    kp2, des2 = sift.detectAndCompute(img2, None)

    # Initialize a brute force matcher, or simply match keypoints between two images based on descriptors
    bf = cv2.BFMatcher()

    # Match descriptors and Store Matches Value (two arrays)
    matches = bf.match(des1, des2) # Matches contains objects represting a matching pair of descriptors

    # Sort matches by distance, the lower the distance the better matches
    matches = sorted(matches, key=lambda x: x.distance)

    # Define a matching threshold, Based on Trial and error 2 works best
    matching_threshold = 2

    # Count the number of matches below the threshold(minimum distance)
    num_matches_below_threshold = sum(1 for match in matches if match.distance < matching_threshold)

    # Determine if the fingerprints match based on the number of matches below the threshold
    if num_matches_below_threshold > 10:  # Set Based on Trial and Error
        print("Fingerprints match ({} matches)".format(num_matches_below_threshold))
        return True
    else:
        print("Fingerprints do not match ({} matches)".format(num_matches_below_threshold))
        return False

def authenticate_finger_print(image_path):
    try:
        # Iterate over each file in the 'fp_db' directory
        folder_path = "fp_db"
        file_list = os.listdir(folder_path)
        for file_name in file_list:
            # Compare the provided image with each file in the directory
            file_path = os.path.join(folder_path, file_name)
            print(f"Comparing '{image_path}' with '{file_path}':")
            if compare_fingerprints(image_path, file_path):
                
                #print("  Match found.")
                return True
            else:
                
                #print("  No match found.")
                continue
        return False
    except Exception as e:
        print("Error comparing with all files:", e)
        
# Example usage:
#print(compare_fingerprints("2_2.bmp", "3_3.bmp"))
def main():
    directory = "fingerprint_bitmaps"
    total_matches = 0
    total_comparisons = 0

    with open('fingerprint_comparisons.csv', mode='w', newline='') as csv_file:
        fieldnames = ['Fingerprint1', 'Fingerprint2', 'Expected_Match', 'Actual_Match']
        writer = csv.DictWriter(csv_file, fieldnames=fieldnames)
        writer.writeheader()

        for i in range(1, 22):
            for j in range(1, 9):
                fingerprint1 = os.path.join(directory, "{}_{}.bmp".format(i, j))
                for k in range(1, 22):
                    for l in range(1, 9):
                        if i == k and j == l:
                            continue  # Skip comparison of same impressions
                        fingerprint2 = os.path.join(directory, "{}_{}.bmp".format(k, l))
                        expected_match = i == k  # True if impressions belong to the same finger
                        actual_match = compare_fingerprints(fingerprint1, fingerprint2)
                        total_comparisons += 1
                        if expected_match == actual_match:
                            total_matches += 1
                        writer.writerow({'Fingerprint1': fingerprint1, 'Fingerprint2': fingerprint2,
                                         'Expected_Match': expected_match, 'Actual_Match': actual_match})
                        print("Comparing {} with {}: Expected match: {}, Actual match: {}".format(
                            fingerprint1, fingerprint2, expected_match, actual_match))

    accuracy = total_matches / total_comparisons * 100
    print("\nTotal Matches:", total_matches)
    print("Total Comparisons:", total_comparisons)
    print("Accuracy: {:.2f}%".format(accuracy))


#main()
    
#print(authenticate_finger_print('1_7.bmp'))
